package com.assignment.spring.rest;

import com.assignment.spring.exceptions.InvalidCityException;
import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.service.ApiConsumerService;
import com.assignment.spring.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class WeatherResource {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherResource.class);

    private final WeatherService weatherService;

    @Autowired
    public WeatherResource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/weather")
    public WeatherEntity weather(@RequestParam(name = "city") final String city) {
        // Alternative to custom validation would be using validations directly on request params.
        if (StringUtils.isEmpty(city)) {
            throw new InvalidCityException("No city specified");
        }

        WeatherEntity entity = weatherService.findWeather(city);
        LOG.debug("Successfully retrieved weather for {}", city);

        return entity;
    }

}
