package com.assignment.spring.service;

import com.assignment.spring.config.AppConfig;
import com.assignment.spring.config.ConfigProperties;
import com.assignment.spring.exceptions.NotFoundException;
import com.assignment.spring.exceptions.WeatherAPIException;
import com.assignment.spring.model.WeatherEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@RestClientTest(ApiConsumerService.class)
@Import({AppConfig.class, ConfigProperties.class})
public class ApiConsumerServiceTest {
    @Autowired
    private ApiConsumerService service;

    private MockRestServiceServer server;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test(expected = NotFoundException.class)
    public void noWeatherForCity() {
        server.expect(MockRestRequestMatchers.requestTo("http://api.openweathermap.org/data/2.5/weather?q=unknown&APPID=123"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
        service.findWeather("unknown");
    }

    @Test(expected = WeatherAPIException.class)
    public void unauthorizedResponse() {
        server.expect(MockRestRequestMatchers.requestTo("http://api.openweathermap.org/data/2.5/weather?q=Nieuwegein&APPID=123"))
                .andRespond(MockRestResponseCreators.withUnauthorizedRequest());
        service.findWeather("Nieuwegein");
    }

    // More testing
}
