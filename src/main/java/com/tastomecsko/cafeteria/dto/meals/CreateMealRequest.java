package com.tastomecsko.cafeteria.dto.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import lombok.Data;

@Data
public class CreateMealRequest {

    private Integer dayId;
    private Integer menuId;
    private String identification;
    private String description;
}
