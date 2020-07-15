package com.assignment.spring.exceptions;

/**
 * Thrown for unrecoverable weather API exception.
 */
public class WeatherAPIException extends RuntimeException {
    private final int code;
    private final String reason;

    public WeatherAPIException(int code, String reason) {
        super();
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
