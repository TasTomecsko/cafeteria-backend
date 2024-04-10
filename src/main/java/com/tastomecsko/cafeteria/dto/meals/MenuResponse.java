package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

import java.util.List;

@Data
public class MenuResponse {
    private Integer menuId;

    private Long selectionStart;
    private Long selectionEnd;

    private Long availableFrom;
    private Long availableTo;

    private List<MealResponse> meals;
}
