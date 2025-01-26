package com.example.demo.services;

import com.example.demo.config.JwtUtil;
import com.example.demo.controller.LoginRequest;
import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private EmployeeRepository employeeRepository;

    public String authenticate(LoginRequest loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (employee != null && encoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            return JwtUtil.generateToken(employee.getEmail()); // Generate the token
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

}
