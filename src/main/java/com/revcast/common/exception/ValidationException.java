package com.revcast.common.exception;

public class ValidationException extends RevCastException {

    public ValidationException(String message) {
        super("ERR_VALIDATION", message);
    }

    public ValidationException(String message, Throwable cause) {
        super("ERR_VALIDATION", message, cause);
    }
}

