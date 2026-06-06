package com.revcast.common.exception;

/**
 * Base exception for RevCast application
 */
public class RevCastException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public RevCastException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public RevCastException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

