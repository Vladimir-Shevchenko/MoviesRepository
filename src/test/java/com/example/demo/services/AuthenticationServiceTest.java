package com.example.demo.services;

import com.example.demo.config.JwtUtil;
import com.example.demo.controller.LoginRequest;
import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Enable Mockito support
public class AuthenticationServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JwtUtil jwtUtil;  // Mock JwtUtil

    @InjectMocks
    private AuthenticationService authenticationService;

    private Employee employee;
    private LoginRequest validLoginRequest;
    private LoginRequest invalidLoginRequest;

    @BeforeEach
    public void setUp() {
        // Create a sample employee with a hashed password
        employee = new Employee();
        employee.setId(1L);
        employee.setEmail("john.doe@example.com");
        employee.setPassword(new BCryptPasswordEncoder().encode("password123"));

        // Create valid and invalid login requests
        validLoginRequest = new LoginRequest("john.doe@example.com", "password123");
        invalidLoginRequest = new LoginRequest("john.doe@example.com", "wrongpassword");
    }

    @Test
    public void testAuthenticateWithValidCredentials() {
        // Mock behavior: find the employee and generate the token
        Employee employee = new Employee();  // make sure employee is properly instantiated
        employee.setEmail("volo@gmail.com");
        employee.setPassword("$2a$10$QIH.I6d6Nfoe3e4BZgGGSubyx175FVE.LGKcKTmXoIeOaX2w0smsS");
        LoginRequest validLoginRequest = new LoginRequest("volo@gmail.com", "password123");

        when(employeeRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(employee);

        // Call the service method
        String token = authenticationService.authenticate(validLoginRequest);

        // Assertions
        assertNotNull(token);
    }

    @Test
    public void testAuthenticateWithInvalidCredentials() {
        // Mock behavior: employee with invalid password
        when(employeeRepository.findByEmail(invalidLoginRequest.getEmail())).thenReturn(employee);

        // Call the service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authenticationService.authenticate(invalidLoginRequest)
        );

        // Assertions
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testAuthenticateWithNonExistentEmployee() {
        // Mock behavior: employee does not exist
        when(employeeRepository.findByEmail(invalidLoginRequest.getEmail())).thenReturn(null);

        // Call the service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authenticationService.authenticate(invalidLoginRequest)
        );

        // Assertions
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
