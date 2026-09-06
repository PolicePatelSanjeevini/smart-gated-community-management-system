package com.smartcommunity.controller;

import com.smartcommunity.dto.response.ApiResponse;
import com.smartcommunity.dto.AuthDTOs.*;
import com.smartcommunity.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & Authorization", description = "APIs for user login, registration, and JWT tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue JWT bearer token")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", tokenResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO registeredUser = authService.registerUser(registerRequest);
        return new ResponseEntity<>(ApiResponse.success("User registered successfully", registeredUser), HttpStatus.CREATED);
    }
}
