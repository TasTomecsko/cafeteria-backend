package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class DeleteMealRequest {

    private Integer mealId;
    private Integer menuId;
}
