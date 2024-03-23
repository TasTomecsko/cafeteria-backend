package com.tastomecsko.cafeteria.services;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToUserResponse;
import com.tastomecsko.cafeteria.dto.user.UserInfoResponse;
import com.tastomecsko.cafeteria.dto.user.UserDataToAdminResponse;
import com.tastomecsko.cafeteria.dto.user.UserUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    public List<UserDataToAdminResponse> getAllUser();

    public void deleteUser(Integer id);

    public UserInfoResponse getUserInfo(JwtRequest request);

    public UserDataToUserResponse getSingleUser(JwtRequest request);
}
