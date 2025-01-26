package com.example.demo.services;

import com.example.demo.dtos.MediaDTO;
import com.example.demo.dtos.MovieAndEmployeeDTO;
import com.example.demo.models.Employee;
import com.example.demo.models.Movie;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MediaService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Fetches and combines movies and series into a unified list of MediaDTO.
     * @return List of MediaDTO
     */
    public List<Object> getAllMedia() {
        List<Object> moviesAndSeries = new ArrayList<>();
        moviesAndSeries.addAll(movieRepository.findAll());
        moviesAndSeries.addAll(seriesRepository.findAll());
        return moviesAndSeries;
    }

    public List<MediaDTO> getSortedMoviesAndSeries(String[] sortBy, String direction) {
        // Validate the direction parameter
        boolean isDescending = "desc".equalsIgnoreCase(direction);
        if (!isDescending && !"asc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException("Invalid direction: " + direction + ". Use 'asc' or 'desc'.");
        }

        List<MediaDTO> combinedList = new ArrayList<>();

        // Add Movies as MediaDTO
        movieRepository.findAll().forEach(movie -> {
            MediaDTO dto = new MediaDTO();
            dto.setId(movie.getId());
            dto.setTitle(movie.getTitle());
            dto.setGenre(movie.getGenre());
            dto.setYear(movie.getYear());
            dto.setDirector(movie.getDirector());
            dto.setDuration(movie.getDuration());
            dto.setAverageRating(movie.getAverageRating());
            dto.setType("Movie");
            combinedList.add(dto);
        });

        // Add Series as MediaDTO
        seriesRepository.findAll().forEach(series -> {
            MediaDTO dto = new MediaDTO();
            dto.setId(series.getId());
            dto.setTitle(series.getTitle());
            dto.setGenre(series.getGenre());
            dto.setYear(series.getYear());
            dto.setDirector(series.getDirector());
            dto.setSeasonCount(series.getSeasonCount());
            dto.setEpisodeCount(series.getEpisodeCount());
            dto.setAverageRating(series.getAverageRating());
            dto.setType("Series");
            combinedList.add(dto);
        });

        // Create comparator and sort
        Comparator<MediaDTO> comparator = createDynamicComparator(sortBy, direction);
        return combinedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }


    public static Comparator<MediaDTO> createDynamicComparator(String[] sortBy, String direction) {
        // Start with a comparator that compares based on the first field.
        Comparator<MediaDTO> comparator = Comparator.comparing(dto -> getValueForSorting(dto, sortBy[0]));

        // Iterate through the rest of the sortBy fields and add them to the comparator.
        for (int i = 1; i < sortBy.length; i++) {
            final int index = i;  // Use a final variable to avoid the lambda variable issue
            comparator = comparator.thenComparing(dto -> getValueForSorting(dto, sortBy[index]));
        }

        // Apply descending order if needed
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private static Comparable getValueForSorting(MediaDTO dto, String field) {
        switch (field.toLowerCase()) {
            case "year":
                return dto.getYear();
            case "title":
                return dto.getTitle();
            case "genre":
                return dto.getGenre();
            case "rating":
                return dto.getAverageRating();
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    // Method to find the highest-rated movie and the employee who proposed it
    public MovieAndEmployeeDTO getBestMovieAndProposer() {
        // Get the movie with the highest average rating
        Movie bestMovie = movieRepository.findTopByOrderByAverageRatingDesc();

        // Find the employee who proposed this movie
        Optional<Employee> proposingEmployee = employeeRepository.findById(bestMovie.getProposedBy());  // Assuming 'proposedBy' stores the employee who proposed the movie

        // Create a DTO or response object to return
        return new MovieAndEmployeeDTO(bestMovie, proposingEmployee.orElse(null));
    }
}
