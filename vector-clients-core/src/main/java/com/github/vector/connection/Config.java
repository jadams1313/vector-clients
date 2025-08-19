package com.github.vector.connection;

import com.google.gson.internal.LinkedTreeMap;

import java.time.Duration;

public class Config {
    private Duration checkInterval;
    private Duration timeout;
    private int maxRetries ;
    private Duration retryDelay;
    private String validationQuery;
    private long warningThresholdMs;
    private long criticalThresholdMs ;
    private boolean autoReconnect;
    private int maxConsecutiveFailures;

    public void setCheckInterval(Duration checkInterval) {
        this.checkInterval = checkInterval;
    }
    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    public void setRetryDelay(Duration retryDelay) {
        this.retryDelay = retryDelay;
    }
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
    public void setWarningThresholdMs(long warningThresholdMs) {
        this.warningThresholdMs = warningThresholdMs;
    }
    public void setCriticalThresholdMs(long criticalThresholdMs) {
        this.criticalThresholdMs = criticalThresholdMs;
    }
    public void setAutoRecconect(boolean autoRecconect) {
        this.autoReconnect = autoRecconect;
    }
    public void setMaxConsecutiveFailures(int maxConsecutiveFailures) {
        this.maxConsecutiveFailures = maxConsecutiveFailures;
    }



    // Getters
    public Duration getCheckInterval() { return checkInterval; }
    public Duration getTimeout() { return timeout; }
    public int getMaxRetries() { return maxRetries; }
    public Duration getRetryDelay() { return retryDelay; }
    public String getValidationQuery() { return validationQuery; }
    public long getWarningThresholdMs() { return warningThresholdMs; }
    public long getCriticalThresholdMs() { return criticalThresholdMs; }
    public boolean isAutoReconnect() { return autoReconnect; }
    public int getMaxConsecutiveFailures() { return maxConsecutiveFailures; }
}
