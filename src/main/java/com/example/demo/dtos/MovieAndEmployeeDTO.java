package com.example.demo.dtos;

import com.example.demo.models.Employee;
import com.example.demo.models.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieAndEmployeeDTO {
    private Movie movie;
    private Employee employee;

    public MovieAndEmployeeDTO(Movie movie, Employee employee) {
        this.movie = movie;
        this.employee = employee;
    }
}