package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class SimpleMealResponse {

    private String identification;
    private String description;
    private Long dayOfMeal;
}
