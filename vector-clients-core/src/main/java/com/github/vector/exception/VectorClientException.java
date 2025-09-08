package com.github.vector.exception;

public class VectorClientException extends Throwable {
    private String message;

    public VectorClientException(String message) {
        this.message = message;
    }

}
