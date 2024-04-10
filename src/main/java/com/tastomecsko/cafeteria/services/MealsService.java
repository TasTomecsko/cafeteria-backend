package com.tastomecsko.cafeteria.services;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.meals.*;

import java.util.List;
import java.util.Map;

public interface MealsService {

    void createMenu(CreateMenuRequest request);

    void modifyMenu(ModifyMenuRequest request);

    void deleteMenu(Integer id);

    List<MenuTimeDetailResponse> getAllMenus();

    List<MenuTimeDetailResponse> getCurrentMenusToSelect();

    MenuResponse getMenuById(Integer menuId);

    MenuResponse createMeal(CreateMealRequest request);

    MenuResponse deleteMeal(DeleteMealRequest request);

    SimpleMealResponse getMealForToday(JwtRequest request);

    void createOrder(CreateOrderRequest request);

    OrdersResponse getOrdersFromMenu(Integer menuId);
}
