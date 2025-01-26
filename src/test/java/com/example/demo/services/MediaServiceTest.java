package com.example.demo.services;

import com.example.demo.dtos.MediaDTO;
import com.example.demo.dtos.MovieAndEmployeeDTO;
import com.example.demo.models.Employee;
import com.example.demo.models.Movie;
import com.example.demo.models.Series;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.SeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private MediaService mediaService;

    private Employee employee;
    private Movie movie;
    private Series series;

    @BeforeEach
    public void setUp() {
        // Create an Employee object
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole("Director");
        employee.setAge(30);

        // Create a Movie object
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setYear(2010);
        movie.setDuration(148);
        movie.setDirector("Christopher Nolan");
        movie.setGenre("Sci-Fi");
        movie.setAverageRating(8.8);
        movie.setProposedBy(1L); // Link to Employee

        // Create a Series object
        series = new Series();
        series.setId(1L);
        series.setTitle("Breaking Bad");
        series.setYear(2008);
        series.setSeasonCount(5);
        series.setEpisodeCount(62);
        series.setDirector("Vince Gilligan");
        series.setGenre("Crime");
        series.setAverageRating(9.5);
        series.setProposedById(1L); // Link to Employee
    }

    @Test
    public void testGetAllMedia() {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie));
        when(seriesRepository.findAll()).thenReturn(Arrays.asList(series));

        // Call service method
        List<Object> allMedia = mediaService.getAllMedia();

        // Assertions
        assertEquals(2, allMedia.size());
        assertTrue(allMedia.contains(movie));
        assertTrue(allMedia.contains(series));
    }

    @Test
    public void testGetSortedMoviesAndSeries() {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie));
        when(seriesRepository.findAll()).thenReturn(Arrays.asList(series));

        // Call service method with sorting by title
        List<MediaDTO> sortedList = mediaService.getSortedMoviesAndSeries(new String[]{"title"}, "asc");

        // Assertions
        assertEquals(2, sortedList.size());
        assertEquals("Breaking Bad", sortedList.get(0).getTitle());  // Sorted by title
        assertEquals("Inception", sortedList.get(1).getTitle());
    }

    @Test
    public void testGetBestMovieAndProposer() {
        when(movieRepository.findTopByOrderByAverageRatingDesc()).thenReturn(movie);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Call service method
        MovieAndEmployeeDTO result = mediaService.getBestMovieAndProposer();

        // Assertions
        assertNotNull(result);
        assertEquals(movie, result.getMovie());
        assertEquals(employee, result.getEmployee());
    }
}
