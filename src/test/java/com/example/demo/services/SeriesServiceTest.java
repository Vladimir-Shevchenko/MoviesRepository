package com.example.demo.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.models.Series;
import com.example.demo.repositories.SeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private SeriesService seriesService;

    // Create common series data for tests
    private Series series;

    @BeforeEach
    public void setup() {
        series = new Series();
        series.setId(1L);
        series.setTitle("Test Series");
        series.setGenre("Drama");
        series.setYear(2021);
        series.setSeasonCount(2);
        series.setEpisodeCount(20);
        series.setAverageRating(8.0);
    }

    @Test
    public void testCalculateAverageRating() {
        // Given
        Long seriesId = 1L;
        when(seriesRepository.findById(seriesId)).thenReturn(Optional.of(series));

        // When
        double averageRating = seriesService.calculateAverageRating(seriesId);

        // Then
        assertEquals(8.0, averageRating);
    }

    @Test
    public void testSaveSeries() {
        // Given
        Series newSeries = new Series();
        newSeries.setTitle("New Series");
        newSeries.setGenre("Comedy");

        when(seriesRepository.save(any(Series.class))).thenReturn(newSeries);

        // When
        Series savedSeries = seriesService.save(newSeries);

        // Then
        assertNotNull(savedSeries);
        assertEquals("New Series", savedSeries.getTitle());
        assertEquals("Comedy", savedSeries.getGenre());
    }

    @Test
    public void testFindAll() {
        // Given
        List<Series> seriesList = new ArrayList<>();
        seriesList.add(series);
        when(seriesRepository.findAll()).thenReturn(seriesList);

        // When
        List<Series> result = seriesService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Series", result.get(0).getTitle());
    }

    @Test
    public void testFindByGenre() {
        // Given
        List<Series> seriesList = new ArrayList<>();
        seriesList.add(series);
        when(seriesRepository.findByGenre("Drama")).thenReturn(seriesList);

        // When
        List<Series> result = seriesService.findByGenre("Drama");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Series", result.get(0).getTitle());
    }

    @Test
    public void testFindByAverageRatingGreaterThan() {
        // Given
        List<Series> seriesList = new ArrayList<>();
        seriesList.add(series);
        when(seriesRepository.findByAverageRatingGreaterThan(7.5)).thenReturn(seriesList);

        // When
        List<Series> result = seriesService.findByAverageRatingGreaterThan(7.5);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Series", result.get(0).getTitle());
    }
}

