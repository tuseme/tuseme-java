package com.tuseme.exceptions;

public class ValidationException extends TusemeException {
    public ValidationException(String message, int statusCode) {
        super(message, statusCode);
    }
}
