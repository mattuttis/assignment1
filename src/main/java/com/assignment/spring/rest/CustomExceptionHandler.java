package com.assignment.spring.rest;

import com.assignment.spring.exceptions.InvalidCityException;
import com.assignment.spring.exceptions.NotFoundException;
import com.assignment.spring.exceptions.ResponseProcessingException;
import com.assignment.spring.exceptions.WeatherAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Central exception handler for the service.
 * This is the right spot for catching the exceptions and processing them.
 * For the sake of this implementation we stick to logging them.
 *
 * Internal or upstream issues are hidden from the client.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(InvalidCityException.class)
    public final ResponseEntity<Object> handleInvalidCityException(InvalidCityException e, WebRequest request) {
        String city = request.getParameter("city");
        LOG.info("No weahter found for city {}", city);
        ErrorResponse error = new ErrorResponse(400, "City is required");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
        String city = request.getParameter("city");
        LOG.info("No weahter found for city {}", city);
        ErrorResponse error = new ErrorResponse(404, "No weather found for city " + city);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WeatherAPIException.class)
    public final ResponseEntity<Object> handleWeatherAPIException(WeatherAPIException e) {
        LOG.error("Failed consuming Weather API. Code {} and reason {}", e.getCode(), e.getReason());
        ErrorResponse error = new ErrorResponse(503, "Service is in maintenance mode");
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResponseProcessingException.class)
    public final ResponseEntity<Object> handleWeatherAPIResponseProcessingException(ResponseProcessingException e) {
        LOG.error("Failed processing Weather API response", e);
        ErrorResponse error = new ErrorResponse(503, "Service is in maintenance mode");
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataAccessException.class)
    public final ResponseEntity<Object> handleDatabaseExceptions(DataAccessException e) {
        LOG.error("Failed processing request because of database issue", e);
        ErrorResponse error = new ErrorResponse(503, "Service is in maintenance mode");
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleUnexpectedExceptions(Exception e) {
        LOG.error("Failed processing request", e);
        ErrorResponse error = new ErrorResponse(503, "Service is in maintenance mode");
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
