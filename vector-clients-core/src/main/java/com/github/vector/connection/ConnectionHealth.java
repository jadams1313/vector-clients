package com.github.vector.connection;

import com.github.vector.connection.Health.Result;
import com.github.vector.connection.Health.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHealth {

    private static final Logger logger = Logger.getLogger(ConnectionHealth.class.getName());



    @FunctionalInterface
    public interface HealthCallback {
        void onHealthChange(Result result);
    }

    private final StorageEngine connectionProvider;
    private final Config config;
    private final ScheduledExecutorService scheduler;
    private final AtomicReference<Result> lastResult = new AtomicReference<>();
    private final AtomicLong consecutiveFailures = new AtomicLong(0);
    private final AtomicBoolean monitoring = new AtomicBoolean(false);
    private final CopyOnWriteArrayList<HealthCallback> callbacks = new CopyOnWriteArrayList<>();

    private volatile ScheduledFuture<?> monitoringTask;

    public ConnectionHealth(StorageEngine connectionProvider) {
        this(connectionProvider, new Config());
    }

    public ConnectionHealth(StorageEngine connectionProvider, Config config) {
        this.connectionProvider = connectionProvider;
        this.config = config;
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "ConnectionHealthMonitor");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Start continuous health monitoring
     */
    public void startMonitoring() {
        if (monitoring.compareAndSet(false, true)) {
            logger.info("Starting connection health monitoring");
            monitoringTask = scheduler.scheduleAtFixedRate(
                    this::performHealthCheck,
                    0,
                    config.getCheckInterval().toMillis(),
                    TimeUnit.MILLISECONDS
            );
        }
    }

    /**
     * Stop health monitoring
     */
    public void stopMonitoring() {
        if (monitoring.compareAndSet(true, false)) {
            logger.info("Stopping connection health monitoring");
            if (monitoringTask != null) {
                monitoringTask.cancel(false);
            }
        }
    }

    /**
     * Perform immediate health check
     */
    public Result checkHealth() {
        return performHealthCheck();
    }

    /**
     * Get the last health check result
     */
    public Result getLastResult() {
        return lastResult.get();
    }

    /**
     * Add health change callback
     */
    public void addHealthCallback(HealthCallback callback) {
        callbacks.add(callback);
    }

    /**
     * Remove health change callback
     */
    public void removeHealthCallback(HealthCallback callback) {
        callbacks.remove(callback);
    }

    private Result performHealthCheck() {
        long startTime = System.currentTimeMillis();
        Result result;

        try {
            result = executeHealthCheckWithRetry();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during health check", e);
            result = new Result(Status.UNHEALTHY,
                    "Unexpected error: " + e.getMessage(),
                    System.currentTimeMillis() - startTime, e);
        }

        updateHealthState(result);
        notifyCallbacks(result);

        return result;
    }

    private Result executeHealthCheckWithRetry() {
        Exception lastException = null;

        for (int attempt = 1; attempt <= config.getMaxRetries(); attempt++) {
            try {
                return executeHealthCheck();
            } catch (Exception e) {
                lastException = e;
                logger.log(Level.WARNING,
                        String.format("Health check attempt %d/%d failed", attempt, config.getMaxRetries()), e);

                if (attempt < config.getMaxRetries()) {
                    try {
                        Thread.sleep(config.getRetryDelay().toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        return new Result(Status.UNHEALTHY,
                "All retry attempts failed: " + (lastException != null ? lastException.getMessage() : "Unknown error"),
                0, lastException);
    }

    private Result executeHealthCheck() throws SQLException {
        long startTime = System.currentTimeMillis();

        try (Connection conn = connectionProvider.getConnection()) {
            if (conn.isClosed()) {
                return new Result(Status.UNHEALTHY, "Connection is closed", 0, null);
            }

            try (PreparedStatement stmt = conn.prepareStatement(config.getValidationQuery())) {
                stmt.setQueryTimeout((int) config.getTimeout().getSeconds());

                try (ResultSet rs = stmt.executeQuery()) {
                    long responseTime = System.currentTimeMillis() - startTime;

                    if (rs.next()) {
                        Status status = determineStatus(responseTime);
                        String message = String.format("Connection healthy (%.2fms)", (double)responseTime);
                        return new Result(status, message, responseTime, null);
                    } else {
                        return new Result(Status.UNHEALTHY,
                                "Validation query returned no results", responseTime, null);
                    }
                }
            }
        } catch (SQLException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            return new Result(Status.UNHEALTHY,
                    "SQL Error: " + e.getMessage(), responseTime, e);
        }
    }

    private Status determineStatus(long responseTime) {
        if (responseTime >= config.getCriticalThresholdMs()) {
            return Status.UNHEALTHY;
        } else if (responseTime >= config.getWarningThresholdMs()) {
            return Status.DEGRADED;
        } else {
            return Status.HEALTHY;
        }
    }

    private void updateHealthState(Result result) {
        Result previous = lastResult.getAndSet(result);

        if (result.getStatus() == Status.UNHEALTHY) {
            long failures = consecutiveFailures.incrementAndGet();
            logger.warning(String.format("Connection unhealthy (%d consecutive failures): %s",
                    failures, result.getMessage()));

            if (config.isAutoReconnect() && failures >= config.getMaxConsecutiveFailures()) {
                logger.warning("Maximum consecutive failures reached, attempting reconnection");
                // Trigger reconnection logic here if needed
            }
        } else if (result.getStatus() == Status.HEALTHY) {
            long previousFailures = consecutiveFailures.getAndSet(0);
            if (previousFailures > 0) {
                logger.info(String.format("Connection recovered after %d failures", previousFailures));
            }
        }
    }

    private void notifyCallbacks(Result result) {
        for (HealthCallback callback : callbacks) {
            try {
                callback.onHealthChange(result);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error in health callback", e);
            }
        }
    }

    /**
     * Get current health statistics
     */
    public HealthStats getHealthStats() {
        Result current = lastResult.get();
        return new HealthStats(
                current != null ? current.getStatus() : Status.UNKNOWN,
                consecutiveFailures.get(),
                monitoring.get()
        );
    }

    public static class HealthStats {
        private final Status currentStatus;
        private final long consecutiveFailures;
        private final boolean monitoring;

        public HealthStats(Status status, long failures, boolean monitoring) {
            this.currentStatus = status;
            this.consecutiveFailures = failures;
            this.monitoring = monitoring;
        }

        public Status getCurrentStatus() { return currentStatus; }
        public long getConsecutiveFailures() { return consecutiveFailures; }
        public boolean isMonitoring() { return monitoring; }
    }

    /**
     * Shutdown the health monitor and cleanup resources
     */
    public void shutdown() {
        stopMonitoring();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
