package com.tastomecsko.cafeteria.controller;

import com.tastomecsko.cafeteria.dto.meals.CreateOrderRequest;
import com.tastomecsko.cafeteria.dto.meals.MenuResponse;
import com.tastomecsko.cafeteria.dto.meals.MenuTimeDetailResponse;
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

    @GetMapping("/testData")
    public ResponseEntity<String> addTestDate() {
        mealsService.createTestData();
        return new ResponseEntity<>("Test data added successfully", HttpStatus.CREATED);
    }
}
