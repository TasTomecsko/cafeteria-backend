package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class MealResponse {
    private Integer mealId;

    private String identification;
    private String description;
}
