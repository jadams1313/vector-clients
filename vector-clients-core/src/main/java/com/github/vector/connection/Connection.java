package com.github.vector.connection;

public interface Connection extends AutoCloseable {

    /**
     * Check if the connection is valid and ready to use
     * @param timeoutSeconds timeout for validation check
     * @return true if connection is valid
     */
    boolean isValid(int timeoutSeconds);

    /**
     * Get the connection ID for tracking and debugging
     * @return unique connection identifier
     */
    String getConnectionId();

    /**
     * Get the database URL or endpoint this connection is connected to
     * @return connection URL/endpoint
     */
    String getEndpoint();

    /**
     * Check if the connection is currently in use
     * @return true if connection is being used
     */
    boolean isInUse();

    /**
     * Mark connection as in use (thread-safe)
     */
    void markInUse();

    /**
     * Mark connection as available (thread-safe)
     */
    void markAvailable();

    /**
     * Get when this connection was created
     * @return creation timestamp in milliseconds
     */
    long getCreatedTimestamp();

    /**
     * Get when this connection was last used
     * @return last used timestamp in milliseconds
     */
    long getLastUsedTimestamp();

    /**
     * Close the connection and release resources
     */
    @Override
    void close();

}
