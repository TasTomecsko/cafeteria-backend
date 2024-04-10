package com.tastomecsko.cafeteria.services.impl;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToUserResponse;
import com.tastomecsko.cafeteria.dto.user.UserInfoResponse;
import com.tastomecsko.cafeteria.dto.user.UserDataToAdminResponse;
import com.tastomecsko.cafeteria.entities.Order;
import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.enums.Role;
import com.tastomecsko.cafeteria.exception.user.FinalAdminDeletionException;
import com.tastomecsko.cafeteria.exception.user.UserNotFoundException;
import com.tastomecsko.cafeteria.repository.OrderRepository;
import com.tastomecsko.cafeteria.repository.UserRepository;
import com.tastomecsko.cafeteria.services.JWTService;
import com.tastomecsko.cafeteria.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JWTService jwtService;

    private final OrderRepository orderRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public List<UserDataToAdminResponse> getAllUser() {
        List<UserDataToAdminResponse> userDataToAdminResponseList = new ArrayList<>();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            userDataToAdminResponseList.add(userToUserDataToAdminResponseDTO(user));
        }

        return userDataToAdminResponseList;
    }

    private UserDataToAdminResponse userToUserDataToAdminResponseDTO(User user) {
        UserDataToAdminResponse response = new UserDataToAdminResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());

        return response;
    }

    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(userToDelete.getRole().equals(Role.ADMIN)) {
            List<User> adminList = userRepository.findAllByRole(Role.ADMIN);

            if(adminList.size() < 2) {
                throw new FinalAdminDeletionException(
                        "Deleting the last user with 'ADMIN' role will result in losing access to admin features!"
                );
            }
        }

        List<Order> userOrders = new ArrayList<>();

        for(int i = 0; i < userToDelete.getOrders().size(); i++) {
            Order order = userToDelete.getOrders().get(i);
            order.setUser(null);
            if(order.getAvailableTo().before(new Date()))
                orderRepository.delete(order);
            else
                userOrders.add(order);
        }

        orderRepository.saveAll(userOrders);

        userRepository.delete(userToDelete);
    }

    public UserInfoResponse getUserInfo(JwtRequest request) {
        String userEmail = jwtService.extractUserName(request.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        UserInfoResponse response = new UserInfoResponse();

        response.setRole(user.getRole());

        return response;
    }

    public UserDataToUserResponse getSingleUser(JwtRequest request) {
        String userEmail = jwtService.extractUserName(request.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        UserDataToUserResponse response = new UserDataToUserResponse();

        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        return response;
    }
}
