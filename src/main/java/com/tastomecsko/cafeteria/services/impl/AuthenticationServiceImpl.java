package com.tastomecsko.cafeteria.services.impl;

import com.tastomecsko.cafeteria.dto.security.JwtAuthenticationResponse;
import com.tastomecsko.cafeteria.dto.security.LoginRequest;
import com.tastomecsko.cafeteria.dto.jwt.RefreshTokenRequest;
import com.tastomecsko.cafeteria.dto.security.SignUpRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToUserResponse;
import com.tastomecsko.cafeteria.dto.user.UserUpdateRequest;
import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.enums.Role;
import com.tastomecsko.cafeteria.exception.user.BadSignupCredentialsException;
import com.tastomecsko.cafeteria.exception.user.BadUserInformationUpdateException;
import com.tastomecsko.cafeteria.repository.UserRepository;
import com.tastomecsko.cafeteria.services.AuthenticationService;
import com.tastomecsko.cafeteria.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public void signup(SignUpRequest signUpRequest) {
        if(!signUpRequestIsValid(signUpRequest)) {
            throw new BadSignupCredentialsException("Invalid values");
        }

        User user = new User();

        if(signUpRequest.getRole() > Role.values().length - 1) {
            throw new BadSignupCredentialsException("Invalid role");
        }
        else if(signUpRequest.getRole() < 0) {
            throw new BadSignupCredentialsException("Invalid role");
        }

        switch (signUpRequest.getRole()) {
            case 0:
                user.setEmail(signUpRequest.getEmail());
                user.setFirstName(signUpRequest.getFirstName());
                user.setLastName(signUpRequest.getLastName());
                user.setRole(Role.USER);
                user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

                userRepository.save(user);
                break;

            case 1:
                user.setEmail(signUpRequest.getEmail());
                user.setFirstName(signUpRequest.getFirstName());
                user.setLastName(signUpRequest.getLastName());
                user.setRole(Role.ADMIN);
                user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

                userRepository.save(user);
                break;
        }
    }

    private boolean signUpRequestIsValid(SignUpRequest request) {
        return request.getFirstName() != null &&
                request.getLastName() != null &&
                request.getEmail() != null &&
                request.getPassword() != null &&
                request.getRole() != null;
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);

            return jwtAuthenticationResponse;
        }
        return null;
    }

    public UserDataToUserResponse updateUser(UserUpdateRequest request) {
        String userEmail = jwtService.extractUserName(request.getToken());
        UserDataToUserResponse response = new UserDataToUserResponse();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        if(isUpdateRequestFilled(request)) {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }
            else {
                throw new BadUserInformationUpdateException("Old password incorrect");
            }
        }
        else {
            throw new BadUserInformationUpdateException("User update details are incomplete");
        }

        final User updated = userRepository.save(user);

        response.setEmail(updated.getEmail());
        response.setFirstName(updated.getFirstName());
        response.setLastName(updated.getLastName());

        return response;
    }

    private boolean isUpdateRequestFilled(UserUpdateRequest request) {
        if(request.getFirstName().isBlank() || request.getFirstName().isEmpty())
            return false;
        if(request.getLastName().isBlank() || request.getLastName().isEmpty())
            return false;
        if(request.getOldPassword().isBlank() || request.getOldPassword().isEmpty())
            return false;
        if(request.getNewPassword().isBlank() || request.getNewPassword().isEmpty())
            return false;

        return true;
    }
}
