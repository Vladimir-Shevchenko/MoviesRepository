package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.Series;
import com.example.demo.models.Vote;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import com.example.demo.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private VoteRepository voteRepository;
    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;

    // Updated method to handle voting logic with employee check
    public void submitVote(Long employeeId, Long movieId, Long seriesId, int voteValue) {
        if (movieId != null) {
            // Check if the employee has already voted for this movie
            if (voteRepository.findByMovieId(movieId.intValue()).stream()
                    .anyMatch(vote -> vote.getEmployeeId() == employeeId.intValue())) {
                throw new RuntimeException("Employee has already voted for this movie.");
            }

            // Voting for a movie
            Vote movieVote = new Vote();
            movieVote.setEmployeeId(employeeId.intValue());
            movieVote.setMovieId(movieId.intValue());
            movieVote.setVoteValue(voteValue);
            voteRepository.save(movieVote);

            updateMovieAverageRating(movieId);  // Recalculate movie average rating
        }

        if (seriesId != null) {
            // Check if the employee has already voted for this series
            if (voteRepository.findBySeriesId(seriesId.intValue()).stream()
                    .anyMatch(vote -> vote.getEmployeeId() == employeeId.intValue())) {
                throw new RuntimeException("Employee has already voted for this series.");
            }

            // Voting for a series
            Vote seriesVote = new Vote();
            seriesVote.setEmployeeId(employeeId.intValue());
            seriesVote.setSeriesId(seriesId.intValue());
            seriesVote.setVoteValue(voteValue);
            voteRepository.save(seriesVote);

            updateSeriesAverageRating(seriesId);  // Recalculate series average rating
        }
    }

    // Method to recalculate the average rating of a movie
    public void updateMovieAverageRating(Long movieId) {
        List<Vote> movieVotes = voteRepository.findByMovieId(movieId.intValue());
        double averageRating = movieVotes.stream()
                .mapToInt(Vote::getVoteValue)
                .average()
                .orElse(0);

        Movie movie = movieRepository.findById(movieId).orElseThrow();
        movie.setAverageRating(averageRating);
        movieRepository.save(movie);
    }

    // Method to recalculate the average rating of a series
    public void updateSeriesAverageRating(Long seriesId) {
        List<Vote> seriesVotes = voteRepository.findBySeriesId(seriesId.intValue());
        double averageRating = seriesVotes.stream()
                .mapToInt(Vote::getVoteValue)
                .average()
                .orElse(0);

        Series series = seriesRepository.findById(seriesId).orElseThrow();
        series.setAverageRating(averageRating);
        seriesRepository.save(series);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> findByMovieId(int movieId) {
        return voteRepository.findByMovieId(movieId);
    }

    public List<Vote> findBySeriesId(int seriesId) {
        return voteRepository.findBySeriesId(seriesId);
    }
}
