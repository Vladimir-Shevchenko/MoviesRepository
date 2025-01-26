package com.example.demo.services;


import com.example.demo.models.Employee;
import com.example.demo.models.Movie;
import com.example.demo.models.Series;
import com.example.demo.models.Vote;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import com.example.demo.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VoteServiceIntegrationTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VoteService voteService;

    private Employee employee;
    private Movie movie;
    private Series series;

    @BeforeEach
    public void setUp() {
        // Create and save an Employee object
        employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole("Director");
        employee.setAge(30);
        employee = employeeRepository.save(employee);

        // Create and save a Movie object
        movie = new Movie();
        movie.setTitle("Inception");
        movie.setYear(2010);
        movie.setDuration(148);
        movie.setDirector("Christopher Nolan");
        movie.setGenre("Sci-Fi");
        movie.setAverageRating(8.8);
        movie.setProposedBy(employee.getId());
        movie.setImplantedBy(employee.getId());
        movie = movieRepository.save(movie);

        // Create and save a Series object
        series = new Series();
        series.setTitle("Breaking Bad");
        series.setYear(2008);
        series.setSeasonCount(5);
        series.setEpisodeCount(62);
        series.setDirector("Vince Gilligan");
        series.setGenre("Crime");
        series.setAverageRating(9.5);
        series.setProposedById(employee.getId());
        series = seriesRepository.save(series);
    }

    @Test
    public void testSubmitVoteForMovie() {
        // Submit a vote for the movie
        voteService.submitVote(employee.getId(), movie.getId(), null, 9);

        // Verify the vote was submitted successfully
        List<Vote> votesForMovie = voteRepository.findByMovieId(movie.getId().intValue());
        assertEquals(1, votesForMovie.size());

        // Check if the average rating is updated
        movie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals(9.0, movie.getAverageRating(), 0.1);
    }

    @Test
    public void testSubmitVoteForSeries() {
        // Submit a vote for the series
        voteService.submitVote(employee.getId(), null, series.getId(), 8);

        // Verify the vote was submitted successfully
        List<Vote> votesForSeries = voteRepository.findBySeriesId(series.getId().intValue());
        assertEquals(1, votesForSeries.size());

        // Check if the average rating is updated
        series = seriesRepository.findById(series.getId()).orElseThrow();
        assertEquals(8.0, series.getAverageRating(), 0.1);
    }

    @Test
    public void testSubmitMultipleVotesForSameMovie() {
        // Submit a vote for the movie
        voteService.submitVote(employee.getId(), movie.getId(), null, 9);

        // Try to submit a vote again for the same movie
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                voteService.submitVote(employee.getId(), movie.getId(), null, 7));

        assertEquals("Employee has already voted for this movie.", exception.getMessage());
    }

    @Test
    public void testSubmitMultipleVotesForSameSeries() {
        // Submit a vote for the series
        voteService.submitVote(employee.getId(), null, series.getId(), 8);

        // Try to submit a vote again for the same series
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                voteService.submitVote(employee.getId(), null, series.getId(), 6));

        assertEquals("Employee has already voted for this series.", exception.getMessage());
    }
}
