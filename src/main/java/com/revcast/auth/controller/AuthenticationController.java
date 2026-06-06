package com.revcast.auth.controller;

import com.revcast.auth.dto.LoginRequest;
import com.revcast.auth.dto.LoginResponse;
import com.revcast.auth.dto.RegisterRequest;
import com.revcast.auth.dto.UserDTO;
import com.revcast.auth.service.AuthenticationService;
import com.revcast.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());
        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user with the provided details and automatically log them in")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) { // Changed return type to LoginResponse
        log.info("Register request for user: {}", request.getUsername());
        LoginResponse loginResponse = authenticationService.register(request); // Changed to LoginResponse
        return new ResponseEntity<>(ApiResponse.success(loginResponse, "User registered and logged in successfully"), HttpStatus.CREATED); // Changed response message
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout user session")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("Logout request");
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }

    @PostMapping("/health")
    @Operation(summary = "Health check", description = "Check if authentication service is healthy")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Service is healthy"));
    }
}
