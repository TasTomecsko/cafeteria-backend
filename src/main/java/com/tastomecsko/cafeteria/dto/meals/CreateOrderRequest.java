package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private String token;

    private Integer menuId;

    private List<Integer> mealIdList;
}
