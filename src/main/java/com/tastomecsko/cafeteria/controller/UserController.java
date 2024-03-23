package com.tastomecsko.cafeteria.controller;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToUserResponse;
import com.tastomecsko.cafeteria.dto.user.UserInfoResponse;
import com.tastomecsko.cafeteria.dto.user.UserUpdateRequest;
import com.tastomecsko.cafeteria.services.AuthenticationService;
import com.tastomecsko.cafeteria.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @PostMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestBody JwtRequest request) {
        return new ResponseEntity<>(userService.getUserInfo(request), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDataToUserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(authenticationService.updateUser(request), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDataToUserResponse> getSingleUser(@RequestBody JwtRequest request) {
        return new ResponseEntity<>(userService.getSingleUser(request), HttpStatus.OK);
    }
}
