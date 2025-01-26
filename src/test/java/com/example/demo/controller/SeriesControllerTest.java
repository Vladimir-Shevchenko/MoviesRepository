package com.example.demo.controller;

import com.example.demo.models.Series;
import com.example.demo.services.SeriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class SeriesControllerTest {

    private MockMvc mockMvc;
    private SeriesService seriesService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        seriesService = mock(SeriesService.class);
        SeriesController seriesController = new SeriesController(seriesService);

        mockMvc = MockMvcBuilders.standaloneSetup(seriesController).build();

        // Register JavaTimeModule for LocalDateTime serialization/deserialization
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testGetAllSeries() throws Exception {
        List<Series> seriesList = Arrays.asList(
                new Series(1L, "Series1", 2022, 3, 30, "Director1", "Drama", 8.5, LocalDateTime.now(), 101L, 201L),
                new Series(2L, "Series2", 2023, 2, 20, "Director2", "Comedy", 7.9, LocalDateTime.now(), 102L, 202L)
        );

        when(seriesService.findAll()).thenReturn(seriesList);

        mockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Series1"))
                .andExpect(jsonPath("$[0].seasonCount").value(3))
                .andExpect(jsonPath("$[0].proposedById").value(101))
                .andExpect(jsonPath("$[1].title").value("Series2"))
                .andExpect(jsonPath("$[1].averageRating").value(7.9));
    }

    @Test
    public void testGetSeriesByGenre() throws Exception {
        List<Series> seriesByGenre = List.of(
                new Series(1L, "Series1", 2022, 3, 30, "Director1", "Drama", 8.5, LocalDateTime.now(), 101L, 201L)
        );

        when(seriesService.findByGenre("Drama")).thenReturn(seriesByGenre);

        mockMvc.perform(get("/api/series/genre").param("genre", "Drama"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Series1"))
                .andExpect(jsonPath("$[0].genre").value("Drama"))
                .andExpect(jsonPath("$[0].proposedById").value(101));
    }

    @Test
    public void testGetSeriesByRating() throws Exception {
        List<Series> seriesByRating = List.of(
                new Series(2L, "Series2", 2023, 2, 20, "Director2", "Comedy", 8.5, LocalDateTime.now(), 102L, 202L)
        );

        when(seriesService.findByAverageRatingGreaterThan(8.0)).thenReturn(seriesByRating);

        mockMvc.perform(get("/api/series/rating").param("rating", "8.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Series2"))
                .andExpect(jsonPath("$[0].averageRating").value(8.5))
                .andExpect(jsonPath("$[0].implantedById").value(202));
    }

    @Test
    public void testCreateSeries() throws Exception {
        Series series = new Series(null, "New Series", 2024, 1, 10, "Director3", "Thriller", 8.0, LocalDateTime.now(), 103L, 203L);
        Series savedSeries = new Series(3L, "New Series", 2024, 1, 10, "Director3", "Thriller", 8.0, LocalDateTime.now(), 103L, 203L);

        when(seriesService.save(series)).thenReturn(savedSeries);

        mockMvc.perform(post("/api/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(series)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSeriesAverageRating() throws Exception {
        when(seriesService.calculateAverageRating(1L)).thenReturn(8.5);

        mockMvc.perform(get("/api/series/1/average-rating"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("8.5"));
    }
}
