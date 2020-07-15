package com.assignment.spring.service;

import com.assignment.spring.config.ConfigProperties;
import com.assignment.spring.exceptions.ResponseProcessingException;
import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.integration.openweatherapi.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service consuming weather API
 */
@Component
public class ApiConsumerService {
    private static final Logger LOG = LoggerFactory.getLogger(ApiConsumerService.class);

    private final RestTemplate restTemplate;
    private final ConfigProperties configProperties;

    @Autowired
    public ApiConsumerService(RestTemplate restTemplate, ConfigProperties configProperties) {
        this.restTemplate = restTemplate;
        this.configProperties = configProperties;
    }

    @Transactional
    public WeatherEntity findWeather(final String city) {
        final String url = composeUrl(city);

        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
        WeatherEntity entity = handleResponse(response);

        LOG.debug("Successfully processed Weather API response for city {}", city);

        return entity;
    }

    private WeatherEntity handleResponse(ResponseEntity<WeatherResponse> response) {
        WeatherEntity entity = null;

        if (response == null) {
            throw new ResponseProcessingException("Weather API response is empty");
        } else {
            entity = mapper(response.getBody());
        }

        return entity;
    }

    private WeatherEntity mapper(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        try {
            entity.setCity(response.getName());
            entity.setCountry(response.getSys().getCountry());
            entity.setTemperature(response.getMain().getTemp());
        } catch (Exception e) {
            throw new ResponseProcessingException(e);
        }
        return entity;
    }

    private String composeUrl(final String city) {
        String url = configProperties.getUrl()
                .replace("{city}", city)
                .replace("{appid}", configProperties.getToken());
        LOG.debug("Composed API URL {}", url);
        return url;
    }
}
