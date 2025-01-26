package com.example.demo.controller;


import com.example.demo.dtos.MediaDTO;
import com.example.demo.dtos.MovieAndEmployeeDTO;
import com.example.demo.models.Employee;
import com.example.demo.models.Movie;
import com.example.demo.services.MediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MediaControllerTest {

    private MockMvc mockMvc;
    private MediaService mediaService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mediaService = mock(MediaService.class); // Manually mock the service
        MediaController mediaController = new MediaController(mediaService);

        mockMvc = MockMvcBuilders.standaloneSetup(mediaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllMedia() throws Exception {
        List<Object> mediaList = Arrays.asList(
                new MediaDTO(1L, "Movie1", "Action", 2023, 120, 0, 0, "Director1", 8.5, "Movie"),
                new MediaDTO(2L, "Series1", "Drama", 2022, 0, 2, 10, "Director2", 9.0, "Series")
        );

        when(mediaService.getAllMedia()).thenReturn(mediaList);

        mockMvc.perform(get("/api/media/allmedia"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Movie1"))
                .andExpect(jsonPath("$[1].title").value("Series1"));
    }

    @Test
    public void testGetSortedMoviesAndSeries() throws Exception {
        List<MediaDTO> sortedMedia = Arrays.asList(
                new MediaDTO(1L, "Movie1", "Action", 2023, 120, 0, 0, "Director1", 8.5, "Movie"),
                new MediaDTO(2L, "Series1", "Drama", 2022, 0, 2, 10, "Director2", 9.0, "Series")
        );

        when(mediaService.getSortedMoviesAndSeries(new String[]{"title"}, "asc")).thenReturn(sortedMedia);

        mockMvc.perform(get("/api/media/movies-series/sorted")
                        .param("sortBy", "title")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Movie1"))
                .andExpect(jsonPath("$[1].title").value("Series1"));
    }

    @Test
    public void testGetBestMovieAndProposer() throws Exception {
        Movie bestMovie = new Movie(1L, "Best Movie", 2023, 120, "Best Director", "Action", 9.5, null, 1L, 2L);
        Employee proposer = new Employee(1L, "John Doe", "john.doe@example.com", "Admin", 30,"");

        MovieAndEmployeeDTO bestMovieAndProposer = new MovieAndEmployeeDTO(bestMovie, proposer);

        when(mediaService.getBestMovieAndProposer()).thenReturn(bestMovieAndProposer);

        mockMvc.perform(get("/api/media/best"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.movie.title").value("Best Movie"))
                .andExpect(jsonPath("$.employee.name").value("John Doe"));
    }
}
