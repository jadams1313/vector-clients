package com.github.vector;

import com.github.vector.connection.ConnectionHealth;
import com.github.vector.connection.ConnectionStatistics;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface StorageEngine {

    /**
     * Get a connection from the pool. Blocks if no connections available
     * until timeout is reached.
     * @return available vector connection
     */
    Connection getConnection() ;

    /**
     * Get a connection asynchronously
     * @return CompletableFuture with the connection
     */
    CompletableFuture<Connection> getConnectionAsync();

    /**
     * Get a connection with specific timeout
     * @param timeout maximum time to wait
     * @param unit time unit
     * @return available connection
     */
    Connection getConnection(long timeout, TimeUnit unit);

    /**
     * Return a connection to the pool after use
     * @param connection the connection to return
     */
    void returnConnection(Connection connection);

    /**
     * Return a connection and mark it as invalid (will be discarded)
     * @param connection the connection to return
     */
    void returnInvalidConnection(Connection connection);

    /**
     * Initialize the connection pool with minimum connections
     * @throws  if initialization fails
     */
    void initialize(int minConnections);

    /**
     * Shutdown the connection manager and close all connections
     * @param graceful if true, wait for active connections to finish
     * @param timeout maximum time to wait for graceful shutdown
     * @param unit time unit for timeout
     */
    void shutdown(boolean graceful, long timeout, TimeUnit unit);

    /**
     * Get current pool statistics
     * @return connection pool statistics
     */
    ConnectionStatistics getStatistics();

    /**
     * Get the current health status of the connection pool
     * @return health status
     */
    ConnectionHealth getHealthStatus();

    /**
     * Validate and cleanup stale connections
     * This should be called periodically by a background thread
     */
    void maintainPool();


    /**
     * Add additional connections to reach target pool size
     * @param targetSize desired pool size
     * @return number of connections added
     */
    int expandPool(int targetSize);

    /**
     * Remove excess connections from the pool
     * @param targetSize desired pool size
     * @return number of connections removed
     */
    int shrinkPool(int targetSize);

    Config getConnectionConfiguration();

}
