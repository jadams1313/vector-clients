package com.github.vector.connection.Health;

import com.github.vector.connection.ConnectionHealth;

import java.time.LocalDateTime;

public class Result {
    private final Status status;
    private final String message;
    private final long responseTimeMs;
    private final LocalDateTime timestamp;
    private final Throwable error;

    public Result(Status status, String message, long responseTimeMs, Throwable error) {
        this.status = status;
        this.message = message;
        this.responseTimeMs = responseTimeMs;
        this.timestamp = LocalDateTime.now();
        this.error = error;
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Throwable getError() { return error; }

    @Override
    public String toString() {
        return String.format("Health[%s]: %s (%.2fms) at %s",
                status, message, (double)responseTimeMs, timestamp);
    }
}
