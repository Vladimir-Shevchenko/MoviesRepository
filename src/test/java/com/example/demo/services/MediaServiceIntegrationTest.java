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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MediaServiceIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MediaService mediaService;

    private Employee employee;
    private Movie movie;
    private Series series;

    @BeforeEach
    public void setUp() {
        // Create an Employee object
        employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole("Director");
        employee.setAge(30);

        // Save employee in the repository
        employee = employeeRepository.save(employee);

        // Create a Movie object
        movie = new Movie(null, "Inception", 2010, 148, "Christopher Nolan", "Sci-Fi", 8.8, LocalDateTime.now(), employee.getId(), employee.getId());
        movie = movieRepository.save(movie);

        // Create a Series object
        series = new Series(null, "Breaking Bad", 2008, 5, 62, "Vince Gilligan", "Crime", 9.5, LocalDateTime.now(), employee.getId(), employee.getId());
        series = seriesRepository.save(series);
    }

    @Test
    public void testGetAllMedia() {
        // Call service method
        List<Object> allMedia = mediaService.getAllMedia();

        // Assertions
        assertTrue(allMedia.size() > 0);
        assertTrue(allMedia.contains(movie));
        assertTrue(allMedia.contains(series));
    }

    @Test
    public void testGetSortedMoviesAndSeries() {
        // Call service method with sorting by title
        List<MediaDTO> sortedList = mediaService.getSortedMoviesAndSeries(new String[]{"title"}, "asc");

        // Assertions
        assertTrue( sortedList.size() > 0);
        assertEquals("Breaking Bad", sortedList.get(0).getTitle());  // Sorted by title
        assertEquals("Inception", sortedList.get(1).getTitle());
    }

    @Test
    public void testGetBestMovieAndProposer() {
        // Call service method
        MovieAndEmployeeDTO result = mediaService.getBestMovieAndProposer();

        // Assertions
        assertNotNull(result);
        assertEquals(movie.getTitle(), result.getMovie().getTitle());
        assertEquals(employee.getName(), result.getEmployee().getName());
    }
}