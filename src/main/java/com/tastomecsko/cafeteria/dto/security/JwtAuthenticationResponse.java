package com.tastomecsko.cafeteria.dto.security;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
}
