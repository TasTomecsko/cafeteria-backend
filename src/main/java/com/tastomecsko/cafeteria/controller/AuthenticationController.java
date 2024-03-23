package com.tastomecsko.cafeteria.controller;

import com.tastomecsko.cafeteria.dto.security.JwtAuthenticationResponse;
import com.tastomecsko.cafeteria.dto.security.LoginRequest;
import com.tastomecsko.cafeteria.dto.jwt.RefreshTokenRequest;
import com.tastomecsko.cafeteria.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse authenticationResponse = authenticationService.login(loginRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtAuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }
}
