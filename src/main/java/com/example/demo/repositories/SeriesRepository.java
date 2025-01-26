package com.example.demo.repositories;

import com.example.demo.models.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    List<Series> findByGenre(String genre);
    List<Series> findByAverageRatingGreaterThan(double rating);
}
