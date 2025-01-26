package com.example.demo.repositories;

import com.example.demo.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(String genre);

    List<Movie> findByAverageRatingGreaterThan(double rating);

    Movie findTopByOrderByAverageRatingDesc();

    List<Movie> findAll();
}
