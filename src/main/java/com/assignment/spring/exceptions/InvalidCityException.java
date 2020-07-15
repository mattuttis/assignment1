package com.assignment.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when validation of city fails.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCityException extends RuntimeException {
    public InvalidCityException(String message) {
        super(message);
    }
}
