package com.tuseme.exceptions;

public class TusemeException extends Exception {
    private final int statusCode;

    public TusemeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() { return statusCode; }
}
