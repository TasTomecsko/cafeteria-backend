package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class ModifyMenuRequest {

    private Integer menuId;

    private Long selectionStart;
    private Long selectionEnd;

    private Long availableFrom;
    private Long availableTo;
}
