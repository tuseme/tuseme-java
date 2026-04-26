package com.tuseme.exceptions;

public class AuthenticationException extends TusemeException {
    public AuthenticationException(String message, int statusCode) {
        super(message, statusCode);
    }
}
