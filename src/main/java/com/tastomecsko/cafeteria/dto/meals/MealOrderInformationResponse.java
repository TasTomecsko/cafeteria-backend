package com.tastomecsko.cafeteria.dto.meals;

import lombok.Data;

@Data
public class MealOrderInformationResponse {

    private SimpleMealResponse meal;
    private Integer numberOfOrders;
}
