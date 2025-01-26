package com.example.demo.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.models.Movie;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
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
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private MovieService movieService;

    // Create common movie data for tests
    private Movie movie;

    @BeforeEach
    public void setup() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setYear(2022);
        movie.setDuration(120);
        movie.setDirector("John Doe");
        movie.setProposedBy(1L);
        movie.setImplantedBy(2L);
        movie.setAverageRating(8.5);
    }

    @Test
    public void testCalculateAverageRating() {
        // Given
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // When
        double averageRating = movieService.calculateAverageRating(movieId);

        // Then
        assertEquals(8.5, averageRating);
    }

    @Test
    public void testSaveMovie() {
        // Given
        Movie newMovie = new Movie();
        newMovie.setTitle("New Movie");
        newMovie.setGenre("Comedy");

        when(movieRepository.save(any(Movie.class))).thenReturn(newMovie);

        // When
        Movie savedMovie = movieService.saveMovie(newMovie);

        // Then
        assertNotNull(savedMovie);
        assertEquals("New Movie", savedMovie.getTitle());
        assertEquals("Comedy", savedMovie.getGenre());
    }

    @Test
    public void testUpdateMovie() {
        // Given
        Long movieId = 1L;
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Updated Movie");
        updatedMovie.setGenre("Drama");
        updatedMovie.setYear(2023);
        updatedMovie.setDuration(150);

        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);
        existingMovie.setTitle("Old Movie");
        existingMovie.setGenre("Action");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);

        // When
        Movie result = movieService.updateMovie(movieId, updatedMovie);

        // Then
        assertNotNull(result);
        assertEquals("Updated Movie", result.getTitle());
        assertEquals("Drama", result.getGenre());
        assertEquals(2023, result.getYear());
        assertEquals(150, result.getDuration());
    }

    @Test
    public void testFindAll() {
        // Given
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        // When
        List<Movie> result = movieService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }

    @Test
    public void testFindByGenre() {
        // Given
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(movieRepository.findByGenre("Action")).thenReturn(movies);

        // When
        List<Movie> result = movieService.findByGenre("Action");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }

    @Test
    public void testFindByAverageRatingGreaterThan() {
        // Given
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(movieRepository.findByAverageRatingGreaterThan(8.0)).thenReturn(movies);

        // When
        List<Movie> result = movieService.findByAverageRatingGreaterThan(8.0);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }
}
