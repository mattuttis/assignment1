package com.assignment.spring.integration;

import com.assignment.spring.exceptions.NotFoundException;
import com.assignment.spring.exceptions.WeatherAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Dedicated error handler for handling API responses.
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                clientHttpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        if (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            handleUnprocessableRequest(clientHttpResponse);
        } else if (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR){
            if (clientHttpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException();
            } else {
                handleUnprocessableRequest(clientHttpResponse);
            }
        }
    }

    private void handleUnprocessableRequest(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus status = clientHttpResponse.getStatusCode();
        int code = status.value();
        String reason = status.getReasonPhrase();
        throw new WeatherAPIException(code, reason);
    }
}
