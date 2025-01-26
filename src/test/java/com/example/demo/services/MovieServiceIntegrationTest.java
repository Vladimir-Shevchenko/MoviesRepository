package com.example.demo.services;


import com.example.demo.models.Movie;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MovieServiceIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    public void setUp() {
        // Create and save a Movie object
        movie = new Movie();
        movie.setTitle("Inception");
        movie.setYear(2010);
        movie.setDuration(148);
        movie.setDirector("Christopher Nolan");
        movie.setGenre("Sci-Fi");
        movie.setAverageRating(8.8);
        movie.setProposedBy(1L);  // Assuming employee ID 1
        movie.setImplantedBy(1L); // Assuming employee ID 1
        movie.setCreatedAt(LocalDateTime.now());

        movie = movieRepository.save(movie);
    }

    @Test
    public void testSaveMovie() {
        // Create a new movie object
        Movie newMovie = new Movie();
        newMovie.setTitle("The Dark Knight");
        newMovie.setYear(2008);
        newMovie.setDuration(152);
        newMovie.setDirector("Christopher Nolan");
        newMovie.setGenre("Action");
        newMovie.setAverageRating(9.0);
        newMovie.setProposedBy(1L);  // Assuming employee ID 1
        newMovie.setImplantedBy(1L); // Assuming employee ID 1

        // Save the new movie using the service
        Movie savedMovie = movieService.saveMovie(newMovie);

        // Assertions
        assertNotNull(savedMovie.getId());
        assertEquals(newMovie.getTitle(), savedMovie.getTitle());
        assertEquals(newMovie.getGenre(), savedMovie.getGenre());
    }

    @Test
    public void testUpdateMovie() {
        // Create an updated movie object
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Inception Updated");
        updatedMovie.setYear(2012);
        updatedMovie.setDuration(150);
        updatedMovie.setDirector("Christopher Nolan");
        updatedMovie.setGenre("Sci-Fi");
        updatedMovie.setAverageRating(9.0);
        updatedMovie.setProposedBy(1L);  // Assuming employee ID 1
        updatedMovie.setImplantedBy(1L); // Assuming employee ID 1

        // Update movie using the service
        Movie updatedMovieResult = movieService.updateMovie(movie.getId(), updatedMovie);

        // Assertions
        assertEquals(updatedMovie.getTitle(), updatedMovieResult.getTitle());
        assertEquals(updatedMovie.getYear(), updatedMovieResult.getYear());
        assertEquals(updatedMovie.getDuration(), updatedMovieResult.getDuration());
        assertEquals(updatedMovie.getDirector(), updatedMovieResult.getDirector());
    }

    @Test
    public void testFindAllMovies() {
        // Call the findAll method from the service
        List<Movie> movies = movieService.findAll();

        // Assertions
        assertTrue(movies.size() > 0);  // Ensure at least one movie is returned
        assertTrue(movies.contains(movie));  // Ensure the movie we created is in the list
    }

    @Test
    public void testFindMoviesByGenre() {
        // Call the findByGenre method from the service
        List<Movie> movies = movieService.findByGenre("Sci-Fi");

        // Assertions
        assertTrue(movies.size() > 0);  // Ensure the list is not empty
        assertTrue(movies.stream().anyMatch(m -> m.getGenre().equals("Sci-Fi")));  // Ensure at least one movie has the genre "Sci-Fi"
    }

    @Test
    public void testFindMoviesByAverageRatingGreaterThan() {
        // Call the findByAverageRatingGreaterThan method from the service
        List<Movie> movies = movieService.findByAverageRatingGreaterThan(8.5);

        // Assertions
        assertTrue(movies.size() > 0);  // Ensure the list is not empty
        assertTrue(movies.stream().anyMatch(m -> m.getAverageRating() > 8.5));  // Ensure at least one movie has an average rating greater than 8.5
    }

    @Test
    public void testCalculateAverageRating() {
        // Call the calculateAverageRating method from the service
        double averageRating = movieService.calculateAverageRating(movie.getId());

        // Assertions
        assertEquals(movie.getAverageRating(), averageRating);
    }
}
