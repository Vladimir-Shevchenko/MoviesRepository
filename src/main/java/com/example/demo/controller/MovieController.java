package com.example.demo.controller;

import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@AllArgsConstructor
public class MovieController {

    private MovieService movieService;

    @Operation(summary = "Get all movies", description = "Fetches all movies from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of movies")
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.findAll();
    }

    @Operation(summary = "Get movies by genre", description = "Fetches movies from the database filtered by genre")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of movies filtered by genre")
    @ApiResponse(responseCode = "400", description = "Invalid genre parameter provided")
    @GetMapping("/genre")
    public List<Movie> getMoviesByGenre(@RequestParam String genre) {
        return movieService.findByGenre(genre);
    }

    @Operation(summary = "Get movies by rating", description = "Fetches movies with average ratings greater than the specified value")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of movies with a rating above the specified value")
    @ApiResponse(responseCode = "400", description = "Invalid rating parameter provided")
    @GetMapping("/rating")
    public List<Movie> getMoviesByRating(@RequestParam double rating) {
        return movieService.findByAverageRatingGreaterThan(rating);
    }

    @Operation(summary = "Create a new movie", description = "Creates a new movie record in the database")
    @ApiResponse(responseCode = "201", description = "Successfully created a new movie")
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @Operation(summary = "Update an existing movie", description = "Updates the details of an existing movie by ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the movie")
    @ApiResponse(responseCode = "404", description = "Movie with the specified ID not found")
    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        return movieService.updateMovie(id, updatedMovie);
    }

    @Operation(summary = "Get average rating of a movie", description = "Fetches the average rating of a movie by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the average rating of the movie")
    @ApiResponse(responseCode = "404", description = "Movie with the specified ID not found")
    @GetMapping("/{id}/average-rating")
    public double getMovieAverageRating(@PathVariable Long id) {
        return movieService.calculateAverageRating(id);
    }
}
