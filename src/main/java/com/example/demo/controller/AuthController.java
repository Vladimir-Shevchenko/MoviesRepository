package com.example.demo.controller;

import com.example.demo.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationService authenticationService;

    @Operation(
            summary = "Login to the system",
            description = "Authenticate and get a JWT token by providing username and password"
    )
    @ApiResponse(responseCode = "200", description = "Successfully logged in, JWT token returned")
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    @PostMapping("/login")
    public String login(
            @RequestBody
            @Parameter(description = "Login request containing username and password")
            LoginRequest loginRequest
    ) {
        return authenticationService.authenticate(loginRequest);
    }
}
