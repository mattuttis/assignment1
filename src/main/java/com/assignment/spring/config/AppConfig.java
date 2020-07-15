package com.assignment.spring.config;

import com.assignment.spring.integration.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Configuration
public class AppConfig {
    @Value("${openweather-api.timeout:500}")
    private int millisTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(new RequestFactorySupplier())
                .errorHandler(errorHandler())
                .build();
    }

    /*
     * Protect users from "hanging" connections.
     */
    public class RequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {
        @Override
        public ClientHttpRequestFactory get() {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(millisTimeout);
            factory.setReadTimeout(millisTimeout);
            return factory;
        }
    }

    @Bean
    public RestTemplateResponseErrorHandler errorHandler() {
        return new RestTemplateResponseErrorHandler();
    }
}
