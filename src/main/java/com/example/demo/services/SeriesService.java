package com.example.demo.services;


import com.example.demo.models.Series;
import com.example.demo.repositories.SeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class SeriesService {

    private SeriesRepository seriesRepository;

    public double calculateAverageRating(Long seriesId) {
        Series series = seriesRepository.findById(seriesId).orElseThrow();
        // Implement logic to calculate average rating
        return series.getAverageRating();
    }

    public List<Series> findAll() {
        return seriesRepository.findAll();
    }

    public List<Series> findByGenre(String genre) {
        return seriesRepository.findByGenre(genre);
    }

    public List<Series> findByAverageRatingGreaterThan(double rating) {
        return seriesRepository.findByAverageRatingGreaterThan(rating);
    }

    public Series save(Series series) {
        return seriesRepository.save(series);
    }
}