package com.assignment.spring.exceptions;

/**
 * Thrown when response from weather API cannot be processed.
 */
public class ResponseProcessingException extends RuntimeException {
    public ResponseProcessingException(Throwable cause) {
        super(cause);
    }

    public ResponseProcessingException(String message) {
        super(message);
    }
}
