package com.example.demo.controller;

import com.example.demo.repositories.MovieRepository;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MovieControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Create and configure ObjectMapper for the test
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule to handle LocalDateTime

        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void testGetAllMovies() throws Exception {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie1", 2023, 120, "Director1", "Action", 8.5, LocalDateTime.now(), 1L, 2L);
        Movie movie2 = new Movie(2L, "Movie2", 2022, 90, "Director2", "Comedy", 7.2, LocalDateTime.now(), 1L, 2L);
        List<Movie> movies = Arrays.asList(movie1, movie2);
        when(movieService.findAll()).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Movie1"))
                .andExpect(jsonPath("$[1].title").value("Movie2"));
    }

    @Test
    public void testGetMoviesByGenre() throws Exception {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie1", 2023, 120, "Director1", "Action", 8.5, LocalDateTime.now(), 1L, 2L);
        List<Movie> movies = Arrays.asList(movie1);
        when(movieService.findByGenre("Action")).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/api/movies/genre")
                        .param("genre", "Action"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Movie1"));
    }

    @Test
    public void testGetMoviesByRating() throws Exception {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie1", 2023, 120, "Director1", "Action", 8.5, LocalDateTime.now(), 1L, 2L);
        List<Movie> movies = Arrays.asList(movie1);
        when(movieService.findByAverageRatingGreaterThan(7.0)).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/api/movies/rating")
                        .param("rating", "7.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Movie1"));
    }

    @Test
    public void testCreateMovie() throws Exception {
        // Arrange
        Movie movie = new Movie(null, "New Movie", 2023, 120, "Director1", "Action", 8.5, LocalDateTime.now(), 1L, 2L);
        when(movieService.saveMovie(movie)).thenReturn(movie);

        // Act & Assert
        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMovie() throws Exception {
        // Arrange
        Movie updatedMovie = new Movie(1L, "Updated Movie", 2023, 110, "Director1", "Drama", 7.8, LocalDateTime.now(), 1L, 2L);
        when(movieService.updateMovie(1L, updatedMovie)).thenReturn(updatedMovie);

        // Act & Assert
        mockMvc.perform(put("/api/movies/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMovieAverageRating() throws Exception {
        // Arrange
        double averageRating = 8.0;
        when(movieService.calculateAverageRating(1L)).thenReturn(averageRating);

        // Act & Assert
        mockMvc.perform(get("/api/movies/{id}/average-rating", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("8.0"));
    }


    @Test
    public void testMovieNotFoundForUpdate() throws Exception {
        // Arrange
        Movie updatedMovie = new Movie(999L, "Updated Movie", 2023, 110, "Director1", "Drama", 7.8, LocalDateTime.now(), 1L, 2L);
        when(movieService.updateMovie(999L, updatedMovie)).thenReturn(null); // Movie not found

        // Act & Assert
        mockMvc.perform(put("/api/moviess/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMovieNotFoundForAverageRating() throws Exception {
        // Arrange
        when(movieService.calculateAverageRating(999L)).thenReturn(0.0); // Movie not found

        // Act & Assert
        mockMvc.perform(get("/api/movies/{id}/average-ratingg", 999))
                .andExpect(status().isNotFound());
    }
}
