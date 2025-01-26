package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.Series;
import com.example.demo.models.Vote;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import com.example.demo.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private VoteService voteService;

    private Vote vote;
    private Movie movie;
    private Series series;

    @BeforeEach
    public void setup() {
        // Initialize common objects for testing
        vote = new Vote();
        vote.setEmployeeId(1);
        vote.setMovieId(1);
        vote.setSeriesId(1);
        vote.setVoteValue(5);

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setAverageRating(0.0);

        series = new Series();
        series.setId(1L);
        series.setTitle("Test Series");
        series.setAverageRating(0.0);
    }

    @Test
    public void testSubmitVoteForMovie() {
        // Mock repository behavior
        when(voteRepository.findByMovieId(1)).thenReturn(Arrays.asList());
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // When
        voteService.submitVote(1L, 1L, null, 5);
        movie.setAverageRating(5.0);

        // Then
        verify(voteRepository, times(1)).save(any(Vote.class)); // Verify that vote was saved
        verify(movieRepository, times(1)).save(any(Movie.class)); // Verify that movie was updated
        assertEquals(5.0, movie.getAverageRating()); // Verify that the movie's average rating was updated
    }

    @Test
    public void testSubmitVoteForSeries() {
        // Mock repository behavior
        when(voteRepository.findBySeriesId(1)).thenReturn(Arrays.asList());
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(series));

        // When
        voteService.submitVote(1L, null, 1L, 7);
        series.setAverageRating(7.0);

        // Then
        verify(voteRepository, times(1)).save(any(Vote.class)); // Verify that vote was saved
        verify(seriesRepository, times(1)).save(any(Series.class)); // Verify that series was updated
        assertEquals(7.0, series.getAverageRating()); // Verify that the series' average rating was updated
    }

    @Test
    public void testSubmitVoteAlreadyVotedForMovie() {
        // Mock repository behavior
        when(voteRepository.findByMovieId(1)).thenReturn(Arrays.asList(vote));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            voteService.submitVote(1L, 1L, null, 5);
        });
        assertEquals("Employee has already voted for this movie.", exception.getMessage());
    }

    @Test
    public void testSubmitVoteAlreadyVotedForSeries() {
        // Mock repository behavior
        when(voteRepository.findBySeriesId(1)).thenReturn(Arrays.asList(vote));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            voteService.submitVote(1L, null, 1L, 7);
        });
        assertEquals("Employee has already voted for this series.", exception.getMessage());
    }

    @Test
    public void testUpdateMovieAverageRating() {
        // Mock repository behavior
        when(voteRepository.findByMovieId(1)).thenReturn(Arrays.asList(vote));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // When
        voteService.updateMovieAverageRating(1L);

        // Then
        assertEquals(5.0, movie.getAverageRating()); // Verify the average rating calculation
    }

    @Test
    public void testUpdateSeriesAverageRating() {
        // Mock repository behavior
        when(voteRepository.findBySeriesId(1)).thenReturn(Arrays.asList(vote));
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(series));

        // When
        voteService.updateSeriesAverageRating(1L);

        // Then
        assertEquals(5.0, series.getAverageRating()); // Verify the average rating calculation
    }
}
