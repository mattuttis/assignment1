package com.assignment.spring.repository;

import com.assignment.spring.model.WeatherEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WeatherRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WeatherRepository repository;

    @Test
    public void saveWeatherShouldBePersisted() {
        WeatherEntity entity = new WeatherEntity();
        entity.setId(1L);
        entity.setCity("Nieuwegein");
        entity.setCountry("Nederland");
        entity.setTemperature(12.0);
        repository.save(entity);

        WeatherEntity result = entityManager.find(WeatherEntity.class, 1L);

        assertEquals(result, entity);
    }
}
