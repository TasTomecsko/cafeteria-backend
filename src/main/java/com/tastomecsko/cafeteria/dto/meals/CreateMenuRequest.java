package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

import java.util.List;

@Data
public class CreateMenuRequest {

    private Long selectionStart;
    private Long selectionEnd;

    private Long availableFrom;
    private Long availableTo;
}
