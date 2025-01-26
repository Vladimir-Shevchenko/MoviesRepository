package com.example.demo.controller;


import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieService movieService;

    private Movie movie;

    private String validToken = "Bearer <your-valid-jwt-token>"; // Replace with your actual JWT token

    @BeforeEach
    public void setUp() {
         movie = new Movie(
                null,
                "New Movie",
                2024,
                120,
                "Director Name",
                "Action",
                0.0,
                LocalDateTime.now(),
                101L,
                201L
        );
    }

    @Test
    public void testCreateMovie() throws Exception {
        Movie savedMovie = movieService.saveMovie(movie); // Save the movie using the real service

        mockMvc.perform(post("/api/movies")
                        .header("Authorization", validToken) // Include the JWT token in the header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().is4xxClientError());
    }
}

