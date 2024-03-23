package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private String token;

    private Integer menuId;

    private Integer mondayMealId;
    private Integer tuesdayMealId;
    private Integer wednesdayMealId;
    private Integer thursdayMealId;
    private Integer fridayMealId;
    private Integer saturdayMealId;
    private Integer sundayMealId;
}
