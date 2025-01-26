package com.example.demo.controller;

import com.example.demo.dtos.MediaDTO;
import com.example.demo.dtos.MovieAndEmployeeDTO;
import com.example.demo.services.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/media")
@AllArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @Operation(summary = "Get all media", description = "Fetches all media (movies and series) from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all media")
    @GetMapping("/allmedia")
    public List<Object> getAllMedia() {
        return mediaService.getAllMedia();
    }

    @Operation(summary = "Get sorted movies and series", description = "Fetches movies and series sorted by specified fields")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the sorted list of movies and series")
    @ApiResponse(responseCode = "400", description = "Invalid sorting parameters provided")
    @GetMapping("/movies-series/sorted")
    public List<MediaDTO> getSortedMoviesAndSeries(
            @RequestParam(value = "sortBy", defaultValue = "meow") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        String[] sortFields = sortBy.split(",");
        sortFields = Arrays.stream(sortFields)  // Convert array to stream
                .map(String::trim)  // Trim spaces from each element
                .toArray(String[]::new);  // Convert back to array
        // Call the service to get the sorted list
        return mediaService.getSortedMoviesAndSeries(sortFields, direction);
    }

    @Operation(summary = "Get the best movie and its proposer", description = "Fetches the best movie and the employee who proposed it")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the best movie and its proposer")
    @GetMapping("/best")
    public MovieAndEmployeeDTO getBestMovieAndProposer() {
        return mediaService.getBestMovieAndProposer();
    }
}
