package com.assignment.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Java representation of app configuration yaml file, including some minimal validations
 */
@Component
@ConfigurationProperties(prefix = "openweather-api")
@Validated
public class ConfigProperties {
    @NotBlank
    private String token;

    @NotBlank
    private String url;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
