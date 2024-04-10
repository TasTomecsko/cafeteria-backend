package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class CreateMealRequest {

    private Integer menuId;
    private String identification;
    private String description;
    private Long dateOfMeal;
}
