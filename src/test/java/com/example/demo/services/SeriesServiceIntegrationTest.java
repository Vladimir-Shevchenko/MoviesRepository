package com.example.demo.services;

import com.example.demo.models.Series;
import com.example.demo.repositories.SeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SeriesServiceIntegrationTest {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    @BeforeEach
    public void setUp() {
        seriesRepository.deleteAll();  // Clean up before each test
    }

    @Test
    public void testSaveSeries() {
        // Arrange
        Series series = new Series(null, "New Series", 2024, 1, 10, "Director", "Action", 9.0, LocalDateTime.now(), 101L, 102L);

        // Act
        Series savedSeries = seriesService.save(series);

        // Assert
        assertNotNull(savedSeries.getId());
        assertNotNull(seriesRepository.findById(savedSeries.getId()));
    }
}

