package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Long id;
    private String title;
    private String genre;
    private int year;
    private int duration;
    private int seasonCount;
    private int episodeCount;
    private String director;
    private double averageRating;
    private String type;
}
