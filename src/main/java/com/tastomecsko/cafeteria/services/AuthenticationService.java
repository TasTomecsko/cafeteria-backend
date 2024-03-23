package com.tastomecsko.cafeteria.services;

import com.tastomecsko.cafeteria.dto.security.JwtAuthenticationResponse;
import com.tastomecsko.cafeteria.dto.security.LoginRequest;
import com.tastomecsko.cafeteria.dto.jwt.RefreshTokenRequest;
import com.tastomecsko.cafeteria.dto.security.SignUpRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToUserResponse;
import com.tastomecsko.cafeteria.dto.user.UserUpdateRequest;

public interface AuthenticationService {

    void signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse login(LoginRequest loginRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    public UserDataToUserResponse updateUser(UserUpdateRequest request);
}
