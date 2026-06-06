package com.revcast.common.exception;

public class ResourceNotFoundException extends RevCastException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("ERR_NOT_FOUND",
                String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String message) {
        super("ERR_NOT_FOUND", message);
    }
}

