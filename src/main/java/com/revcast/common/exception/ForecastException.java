package com.revcast.common.exception;

public class ForecastException extends RevCastException {

    public ForecastException(String message) {
        super("ERR_FORECAST", message);
    }

    public ForecastException(String message, Throwable cause) {
        super("ERR_FORECAST", message, cause);
    }
}

