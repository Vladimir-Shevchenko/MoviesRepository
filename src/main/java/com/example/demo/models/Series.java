package com.example.demo.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "series", schema = "idrica_test")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int year;
    private int seasonCount;
    private int episodeCount;
    private String director;
    private String genre;
    private double averageRating;
    private LocalDateTime createdAt = LocalDateTime.now();;
    private Long proposedById;  // Changed to Long to reference the Employee ID
    private Long implantedById; // Changed to Long to reference the Employee ID

    // Getters and Setters
}
