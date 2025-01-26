package com.example.demo.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "vote", schema = "idrica_test")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_id", nullable = false)
    private int employeeId;  // Store employee ID instead of Employee entity

    @Column(name = "movie_id")
    private int movieId;  // Store movie ID (nullable)

    @Column(name = "series_id")
    private int seriesId;  // Store series ID (nullable)

    private int voteValue;  // A score from 1 to 5 or similar

    private LocalDateTime votedAt;
}
