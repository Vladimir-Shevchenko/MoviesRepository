package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {

    private MovieRepository movieRepository;
    private SeriesRepository seriesRepository;
    private EmployeeRepository employeeRepository;

    public double calculateAverageRating(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        // Implement logic to calculate average rating
        return movie.getAverageRating();
    }

    public Movie saveMovie(Movie movie) {
        // Simply save the movie, since you are already storing the employee IDs as long
        return movieRepository.save(movie);
    }

    // Method to update movie data
    public Movie updateMovie(Long id, Movie updatedMovie) {
        // Fetch the existing movie
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));

        // Update the fields (adjust as needed for which fields you want to update)
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setYear(updatedMovie.getYear());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setProposedBy(updatedMovie.getProposedBy());
        existingMovie.setImplantedBy(updatedMovie.getImplantedBy());

        // Save the updated movie
        return movieRepository.save(existingMovie);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<Movie> findByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public List<Movie> findByAverageRatingGreaterThan(double rating) {
        return movieRepository.findByAverageRatingGreaterThan(rating);
    }
}
