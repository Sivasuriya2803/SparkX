package com.revcast.common.exception;

public class UnauthorizedException extends RevCastException {

    public UnauthorizedException(String message) {
        super("ERR_UNAUTHORIZED", message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super("ERR_UNAUTHORIZED", message, cause);
    }
}

