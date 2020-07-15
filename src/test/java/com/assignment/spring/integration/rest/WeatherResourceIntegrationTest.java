package com.assignment.spring.integration.rest;

import com.assignment.spring.exceptions.NotFoundException;
import com.assignment.spring.exceptions.ResponseProcessingException;
import com.assignment.spring.exceptions.WeatherAPIException;
import com.assignment.spring.model.WeatherEntity;
import com.assignment.spring.rest.WeatherResource;
import com.assignment.spring.service.ApiConsumerService;
import com.assignment.spring.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherResource.class)
public class WeatherResourceIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherService service;

    @Test
    public void givenWeather_WhenFindWeather_thenReturnJson() throws Exception {
        WeatherEntity entity = new WeatherEntity();
        entity.setId(1L);
        entity.setCountry("NL");
        entity.setCity("Nieuwegein");
        entity.setTemperature(12.0);

        BDDMockito.given(service.findWeather("Nieuwegein")).willReturn(entity);

        mvc.perform(get("/v1/weather?city=Nieuwegein")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.country", is("NL")))
                .andExpect(jsonPath("$.city", is("Nieuwegein")))
                .andExpect(jsonPath("$.temperature", is(12.0)));
    }

    @Test
    public void noCountryProvidedResultsInBadRequest() throws Exception {
        mvc.perform(get("/v1/weather?city=")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    public void noResultFoundForUnknownCityResultsInNotFound() throws Exception {
        BDDMockito.given(service.findWeather("xx")).willThrow(new NotFoundException());

        mvc.perform(get("/v1/weather?city=xx")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void anyWeatherAPIExceptionResultsInResourceUnavailable() throws Exception {
        BDDMockito.given(service.findWeather("Nieuwegein")).willThrow(new WeatherAPIException(500, "Some error"));

        mvc.perform(get("/v1/weather?city=Nieuwegein")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status", is(503)));
    }

    @Test
    public void apiResponseProcessingFailureResultsInResourceUnavailable() throws Exception {
        BDDMockito.given(service.findWeather("Nieuwegein")).willThrow(new ResponseProcessingException(new RuntimeException()));

        mvc.perform(get("/v1/weather?city=Nieuwegein")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status", is(503)));
    }

    @Test
    public void unexpectedExceptionsResultInResourceUnavailable() throws Exception {
        BDDMockito.given(service.findWeather("Nieuwegein")).willThrow(new RuntimeException());

        mvc.perform(get("/v1/weather?city=Nieuwegein")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status", is(503)));
    }
}
