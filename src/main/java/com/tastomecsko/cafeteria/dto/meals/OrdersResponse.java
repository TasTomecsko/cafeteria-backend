package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponse {

    private List<MealOrderInformationResponse> orderedMeals;
}
