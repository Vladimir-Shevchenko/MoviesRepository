package com.example.demo.controller;

import com.example.demo.models.Series;
import com.example.demo.services.SeriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SeriesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SeriesService seriesService;

    private Series series;

    private String validToken = "Bearer <your-valid-jwt-token>"; // Replace with a valid JWT

    @BeforeEach
    public void setUp() {
        series = new Series(null, "New Series", 2024, 1, 10, "Director3", "Thriller", 8.0, null, 103L, 203L);
    }

    @Test
    public void testCreateSeriesAnauthorized() throws Exception {
        Series savedSeries = seriesService.save(series); // Save the series using the real service

        mockMvc.perform(post("/api/series")
                        .header("Authorization", validToken) // Add the valid JWT token in the header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(series)))
                .andExpect(status().is4xxClientError());
    }
}

