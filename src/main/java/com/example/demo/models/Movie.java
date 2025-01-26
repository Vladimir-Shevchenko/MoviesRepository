package com.example.demo.models;



import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "movies", schema = "idrica_test")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private int year;

    private int duration;

    private String director;

    private String genre;

    @Column(name = "average_rating", precision = 3, scale = 2)
    @JsonProperty("average_rating")
    private double averageRating;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();;

    @JoinColumn(name = "proposed_by")
    @JsonProperty("proposed_by")
    private long proposedBy;
    @JoinColumn(name = "implanted_by")
    @JsonProperty("implanted_by")
    private long implantedBy;
    // You can also include default constructors, equals(), and hashCode() if necessary
}
