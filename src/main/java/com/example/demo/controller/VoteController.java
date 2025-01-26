package com.example.demo.controller;


import com.example.demo.models.Vote;
import com.example.demo.services.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private VoteService voteService;

    @Operation(summary = "Create a new vote", description = "Creates a new vote record for a movie or series")
    @ApiResponse(responseCode = "201", description = "Successfully created a new vote")
    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.save(vote);
    }

    @Operation(summary = "Get all votes for a movie", description = "Fetches all votes for a specific movie by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the votes for the movie")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @GetMapping("/movie/{movieId}")
    public List<Vote> getVotesForMovie(@PathVariable int movieId) {
        return voteService.findByMovieId(movieId);
    }

    @Operation(summary = "Get all votes for a series", description = "Fetches all votes for a specific series by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the votes for the series")
    @ApiResponse(responseCode = "404", description = "Series not found")
    @GetMapping("/series/{seriesId}")
    public List<Vote> getVotesForSeries(@PathVariable int seriesId) {
        return voteService.findBySeriesId(seriesId);
    }

    @Operation(summary = "Submit a vote for a movie or series", description = "Allows an employee to submit a vote for a movie or series")
    @ApiResponse(responseCode = "200", description = "Vote successfully submitted")
    @ApiResponse(responseCode = "400", description = "Invalid vote value (must be between 1 and 5)")
    @ApiResponse(responseCode = "404", description = "Movie or series not found")
    @PostMapping("/vote")
    public ResponseEntity<String> submitVote(
            @RequestParam Long employeeId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long seriesId,
            @RequestParam int voteValue) {

        // Validate the vote value (assuming 1 to 5)
        if (voteValue < 1 || voteValue > 5) {
            return ResponseEntity.badRequest().body("Invalid vote value. It must be between 1 and 5.");
        }

        // Submit the vote
        voteService.submitVote(employeeId, movieId, seriesId, voteValue);

        return ResponseEntity.ok("Vote submitted successfully!");
    }
}
