package com.tastomecsko.cafeteria.controller;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.meals.*;
import com.tastomecsko.cafeteria.services.MealsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MealsController {

    private final MealsService mealsService;

    @GetMapping
    public ResponseEntity<List<MenuTimeDetailResponse>> getCurrentMenuToSelect() {
        return new ResponseEntity<>(mealsService.getCurrentMenusToSelect(), HttpStatus.OK);
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuResponse> getMenuToSelectById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(mealsService.getMenuById(id), HttpStatus.OK);
    }

    @PostMapping("/order/create")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        mealsService.createOrder(request);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/order/meal")
    public ResponseEntity<SimpleMealResponse> getMealForToday(@RequestBody JwtRequest request) {
        return new ResponseEntity<>(mealsService.getMealForToday(request), HttpStatus.OK);
    }
}