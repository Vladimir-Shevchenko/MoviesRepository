package com.example.demo.controller;


import com.example.demo.models.Series;
import com.example.demo.services.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@AllArgsConstructor
public class SeriesController {

    private SeriesService seriesService;

    @Operation(summary = "Get all series", description = "Fetches all series from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of series")
    @GetMapping
    public List<Series> getAllSeries() {
        return seriesService.findAll();
    }

    @Operation(summary = "Get series by genre", description = "Fetches series from the database filtered by genre")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of series filtered by genre")
    @ApiResponse(responseCode = "400", description = "Invalid genre parameter provided")
    @GetMapping("/genre")
    public List<Series> getSeriesByGenre(@RequestParam String genre) {
        return seriesService.findByGenre(genre);
    }

    @Operation(summary = "Get series by rating", description = "Fetches series with average ratings greater than the specified value")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of series with a rating above the specified value")
    @ApiResponse(responseCode = "400", description = "Invalid rating parameter provided")
    @GetMapping("/rating")
    public List<Series> getSeriesByRating(@RequestParam double rating) {
        return seriesService.findByAverageRatingGreaterThan(rating);
    }

    @Operation(summary = "Create a new series", description = "Creates a new series record in the database")
    @ApiResponse(responseCode = "201", description = "Successfully created a new series")
    @PostMapping
    public Series createSeries(@RequestBody Series series) {
        return seriesService.save(series);
    }

    @Operation(summary = "Get average rating of a series", description = "Fetches the average rating of a series by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the average rating of the series")
    @ApiResponse(responseCode = "404", description = "Series with the specified ID not found")
    @GetMapping("/{id}/average-rating")
    public double getSeriesAverageRating(@PathVariable Long id) {
        return seriesService.calculateAverageRating(id);
    }
}
