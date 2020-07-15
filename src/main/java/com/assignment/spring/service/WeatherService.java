package com.assignment.spring.service;

import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeatherService {
    private final ApiConsumerService apiConsumerService;
    private final WeatherRepository repository;

    public WeatherService(ApiConsumerService apiConsumerService, WeatherRepository repository) {
        this.apiConsumerService = apiConsumerService;
        this.repository = repository;
    }

    @Transactional
    public WeatherEntity findWeather(String city) {
        WeatherEntity entity = apiConsumerService.findWeather(city);
        repository.save(entity);
        return entity;
    }
}
