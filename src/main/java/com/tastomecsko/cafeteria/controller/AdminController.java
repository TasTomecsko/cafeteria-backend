package com.tastomecsko.cafeteria.controller;

import com.tastomecsko.cafeteria.dto.meals.*;
import com.tastomecsko.cafeteria.dto.security.SignUpRequest;
import com.tastomecsko.cafeteria.dto.user.UserDataToAdminResponse;
import com.tastomecsko.cafeteria.services.AuthenticationService;
import com.tastomecsko.cafeteria.services.MealsService;
import com.tastomecsko.cafeteria.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final MealsService mealsService;

    @GetMapping
    public ResponseEntity<List<UserDataToAdminResponse>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody SignUpRequest request) {
        authenticationService.signup(request);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/menu/create")
    public ResponseEntity<String> createMenu(@RequestBody CreateMenuRequest request) {
        mealsService.createMenu(request);
        return new ResponseEntity<>("Menu created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/menu/modify")
    public ResponseEntity<String> modifyMenu(@RequestBody ModifyMenuRequest request) {
        mealsService.modifyMenu(request);
        return new ResponseEntity<>("Menu successfully modified", HttpStatus.OK);
    }

    @PostMapping("/menu/meal/create")
    public ResponseEntity<MenuResponse> createMeal(@RequestBody CreateMealRequest request) {
        return new ResponseEntity<>(mealsService.createMeal(request), HttpStatus.CREATED);
    }

    @PostMapping("/menu/meal/delete")
    public ResponseEntity<MenuResponse> deleteMeal(@RequestBody DeleteMealRequest request) {
        return new ResponseEntity<>(mealsService.deleteMeal(request), HttpStatus.OK);
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable(value = "id") Integer id) {
        mealsService.deleteMenu(id);
        return new ResponseEntity<>("Menu deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<MenuTimeDetailResponse>> getAllMenus() {
        return new ResponseEntity<>(mealsService.getAllMenus(), HttpStatus.OK);
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(mealsService.getMenuById(id), HttpStatus.OK);
    }

    @GetMapping("/menu/{id}/orders")
    public ResponseEntity<OrdersResponse> getOrders(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(mealsService.getOrdersFromMenu(id), HttpStatus.OK);
    }
}
