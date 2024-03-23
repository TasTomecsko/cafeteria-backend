package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class MenuTimeDetailResponse {
    private Integer menuId;

    private Long selectionStart;
    private Long selectionEnd;

    private Long availableFrom;
    private Long availableTo;
}
