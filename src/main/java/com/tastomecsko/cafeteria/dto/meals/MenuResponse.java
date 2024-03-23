package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuResponse {
    private Integer menuId;

    private Long selectionStart;
    private Long selectionEnd;

    private Long availableFrom;
    private Long availableTo;

    private List<MealResponse> mondayMeals;
    private List<MealResponse> tuesdayMeals;
    private List<MealResponse> wednesdayMeals;
    private List<MealResponse> thursdayMeals;
    private List<MealResponse> fridayMeals;
    private List<MealResponse> saturdayMeals;
    private List<MealResponse> sundayMeals;
}
