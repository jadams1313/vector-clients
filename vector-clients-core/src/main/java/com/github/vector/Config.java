package com.github.vector;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.util.*;

public class Config {
    // === Core Connection Properties ===
    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("apiKey")
    private String apiKey;

    @JsonProperty("port")
    private Integer port;

    @JsonProperty("serverName")
    private String serverName;

    @JsonProperty("databaseName")
    private String databaseName;

    @JsonProperty("environment")
    private String environment; // For Pinecone: us-west1-gcp, etc.

    // === Connection Pool Settings ===
    @JsonProperty("maxPoolSize")
    private int maxPoolSize = 10;

    @JsonProperty("minPoolSize")
    private int minPoolSize = 2;

    @JsonProperty("connectionTimeout")
    private Duration connectionTimeout = Duration.ofSeconds(30);

    @JsonProperty("idleTimeout")
    private Duration idleTimeout = Duration.ofMinutes(10);

    @JsonProperty("maxConnectionLifetime")
    private Duration maxConnectionLifetime = Duration.ofHours(1);

    // === Health Monitoring Settings ===
    @JsonProperty("checkInterval")
    private Duration checkInterval = Duration.ofSeconds(30);

    @JsonProperty("validationTimeout")
    private Duration validationTimeout = Duration.ofSeconds(5);

    @JsonProperty("validationQuery")
    private String validationQuery;

    @JsonProperty("warningThresholdMs")
    private long warningThresholdMs = 1000;

    @JsonProperty("criticalThresholdMs")
    private long criticalThresholdMs = 5000;

    // === Retry and Resilience Settings ===
    @JsonProperty("maxRetries")
    private int maxRetries = 3;

    @JsonProperty("retryDelay")
    private Duration retryDelay = Duration.ofSeconds(1);

    @JsonProperty("backoffMultiplier")
    private double backoffMultiplier = 2.0;

    @JsonProperty("maxRetryDelay")
    private Duration maxRetryDelay = Duration.ofSeconds(30);

    @JsonProperty("autoReconnect")
    private boolean autoReconnect = true;

    @JsonProperty("maxConsecutiveFailures")
    private int maxConsecutiveFailures = 5;

    // === Request Settings ===
    @JsonProperty("requestTimeout")
    private Duration requestTimeout = Duration.ofSeconds(60);

    @JsonProperty("readTimeout")
    private Duration readTimeout = Duration.ofSeconds(30);

    @JsonProperty("writeTimeout")
    private Duration writeTimeout = Duration.ofSeconds(30);

    // === SSL/TLS Settings ===
    @JsonProperty("sslEnabled")
    private boolean sslEnabled = true;

    @JsonProperty("sslVerifyHostname")
    private boolean sslVerifyHostname = true;

    @JsonProperty("trustStorePath")
    private String trustStorePath;

    @JsonProperty("trustStorePassword")
    private String trustStorePassword;

    // === Database Type ===
    @JsonProperty("databaseType")
    private String databaseType; // "pinecone", "weaviate", "qdrant", etc.

    @JsonProperty("apiVersion")
    private String apiVersion = "v1";

    // === Extensibility Support ===
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    // Default constructor
    public Config() {}

    // === Core Connection Getters/Setters ===
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    // === Connection Pool Getters/Setters ===
    public int getMaxPoolSize() { return maxPoolSize; }
    public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }

    public int getMinPoolSize() { return minPoolSize; }
    public void setMinPoolSize(int minPoolSize) { this.minPoolSize = minPoolSize; }

    public Duration getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(Duration connectionTimeout) { this.connectionTimeout = connectionTimeout; }

    public Duration getIdleTimeout() { return idleTimeout; }
    public void setIdleTimeout(Duration idleTimeout) { this.idleTimeout = idleTimeout; }

    public Duration getMaxConnectionLifetime() { return maxConnectionLifetime; }
    public void setMaxConnectionLifetime(Duration maxConnectionLifetime) { this.maxConnectionLifetime = maxConnectionLifetime; }

    // === Health Monitoring Getters/Setters ===
    public Duration getCheckInterval() { return checkInterval; }
    public void setCheckInterval(Duration checkInterval) { this.checkInterval = checkInterval; }

    public Duration getValidationTimeout() { return validationTimeout; }
    public void setValidationTimeout(Duration validationTimeout) { this.validationTimeout = validationTimeout; }

    public String getValidationQuery() { return validationQuery; }
    public void setValidationQuery(String validationQuery) { this.validationQuery = validationQuery; }

    public long getWarningThresholdMs() { return warningThresholdMs; }
    public void setWarningThresholdMs(long warningThresholdMs) { this.warningThresholdMs = warningThresholdMs; }

    public long getCriticalThresholdMs() { return criticalThresholdMs; }
    public void setCriticalThresholdMs(long criticalThresholdMs) { this.criticalThresholdMs = criticalThresholdMs; }

    // === Retry Getters/Setters ===
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    public Duration getRetryDelay() { return retryDelay; }
    public void setRetryDelay(Duration retryDelay) { this.retryDelay = retryDelay; }

    public double getBackoffMultiplier() { return backoffMultiplier; }
    public void setBackoffMultiplier(double backoffMultiplier) { this.backoffMultiplier = backoffMultiplier; }

    public Duration getMaxRetryDelay() { return maxRetryDelay; }
    public void setMaxRetryDelay(Duration maxRetryDelay) { this.maxRetryDelay = maxRetryDelay; }

    public boolean isAutoReconnect() { return autoReconnect; }
    public void setAutoReconnect(boolean autoReconnect) { this.autoReconnect = autoReconnect; }

    public int getMaxConsecutiveFailures() { return maxConsecutiveFailures; }
    public void setMaxConsecutiveFailures(int maxConsecutiveFailures) { this.maxConsecutiveFailures = maxConsecutiveFailures; }

    // === Request Settings Getters/Setters ===
    public Duration getRequestTimeout() { return requestTimeout; }
    public void setRequestTimeout(Duration requestTimeout) { this.requestTimeout = requestTimeout; }

    public Duration getReadTimeout() { return readTimeout; }
    public void setReadTimeout(Duration readTimeout) { this.readTimeout = readTimeout; }

    public Duration getWriteTimeout() { return writeTimeout; }
    public void setWriteTimeout(Duration writeTimeout) { this.writeTimeout = writeTimeout; }

    // === SSL Getters/Setters ===
    public boolean isSslEnabled() { return sslEnabled; }
    public void setSslEnabled(boolean sslEnabled) { this.sslEnabled = sslEnabled; }

    public boolean isSslVerifyHostname() { return sslVerifyHostname; }
    public void setSslVerifyHostname(boolean sslVerifyHostname) { this.sslVerifyHostname = sslVerifyHostname; }

    public String getTrustStorePath() { return trustStorePath; }
    public void setTrustStorePath(String trustStorePath) { this.trustStorePath = trustStorePath; }

    public String getTrustStorePassword() { return trustStorePassword; }
    public void setTrustStorePassword(String trustStorePassword) { this.trustStorePassword = trustStorePassword; }

    // === Database Type Getters/Setters ===
    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }

    public String getApiVersion() { return apiVersion; }
    public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }

    // === Extensibility Methods ===
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public <T> Optional<T> getAdditionalProperty(String name, Class<T> type) {
        Object value = additionalProperties.get(name);
        if (value != null && type.isInstance(value)) {
            return Optional.of(type.cast(value));
        }
        return Optional.empty();
    }

    public void removeAdditionalProperty(String name) {
        additionalProperties.remove(name);
    }

    // === Validation Methods ===
    public void validate() throws IllegalStateException {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new IllegalStateException("Endpoint cannot be null or empty");
        }

        if (databaseType == null || databaseType.trim().isEmpty()) {
            throw new IllegalStateException("Database type must be specified");
        }

        if (maxPoolSize <= 0) {
            throw new IllegalStateException("Max pool size must be positive");
        }

        if (minPoolSize < 0 || minPoolSize > maxPoolSize) {
            throw new IllegalStateException("Min pool size must be between 0 and max pool size");
        }

        if (maxRetries < 0) {
            throw new IllegalStateException("Max retries cannot be negative");
        }

        if (maxConsecutiveFailures <= 0) {
            throw new IllegalStateException("Max consecutive failures must be positive");
        }
    }

    // === Utility Methods ===
    public Config copy() {
        Config copy = new Config();

        // Copy all properties
        copy.endpoint = this.endpoint;
        copy.apiKey = this.apiKey;
        copy.port = this.port;
        copy.serverName = this.serverName;
        copy.databaseName = this.databaseName;
        copy.environment = this.environment;

        copy.maxPoolSize = this.maxPoolSize;
        copy.minPoolSize = this.minPoolSize;
        copy.connectionTimeout = this.connectionTimeout;
        copy.idleTimeout = this.idleTimeout;
        copy.maxConnectionLifetime = this.maxConnectionLifetime;

        copy.checkInterval = this.checkInterval;
        copy.validationTimeout = this.validationTimeout;
        copy.validationQuery = this.validationQuery;
        copy.warningThresholdMs = this.warningThresholdMs;
        copy.criticalThresholdMs = this.criticalThresholdMs;

        copy.maxRetries = this.maxRetries;
        copy.retryDelay = this.retryDelay;
        copy.backoffMultiplier = this.backoffMultiplier;
        copy.maxRetryDelay = this.maxRetryDelay;
        copy.autoReconnect = this.autoReconnect;
        copy.maxConsecutiveFailures = this.maxConsecutiveFailures;

        copy.requestTimeout = this.requestTimeout;
        copy.readTimeout = this.readTimeout;
        copy.writeTimeout = this.writeTimeout;

        copy.sslEnabled = this.sslEnabled;
        copy.sslVerifyHostname = this.sslVerifyHostname;
        copy.trustStorePath = this.trustStorePath;
        copy.trustStorePassword = this.trustStorePassword;

        copy.databaseType = this.databaseType;
        copy.apiVersion = this.apiVersion;

        // Deep copy additional properties
        copy.additionalProperties.putAll(this.additionalProperties);

        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Config that = (Config) obj;

        return Objects.equals(endpoint, that.endpoint) &&
                Objects.equals(apiKey, that.apiKey) &&
                Objects.equals(databaseType, that.databaseType) &&
                maxPoolSize == that.maxPoolSize &&
                minPoolSize == that.minPoolSize &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, apiKey, databaseType, maxPoolSize, minPoolSize, additionalProperties);
    }

    @Override
    public String toString() {
        return "VectorDatabaseConfig{" +
                "endpoint='" + endpoint + '\'' +
                ", databaseType='" + databaseType + '\'' +
                ", maxPoolSize=" + maxPoolSize +
                ", minPoolSize=" + minPoolSize +
                ", additionalPropertiesCount=" + additionalProperties.size() +
                '}';
    }
}
