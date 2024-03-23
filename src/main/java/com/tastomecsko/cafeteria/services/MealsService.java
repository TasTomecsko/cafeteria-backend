package com.tastomecsko.cafeteria.services;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.meals.*;

import java.util.List;

public interface MealsService {

    public void createMenu(CreateMenuRequest request);

    public void modifyMenu(ModifyMenuRequest request);

    public void deleteMenu(Integer id);

    public List<MenuTimeDetailResponse> getAllMenus();

    public List<MenuTimeDetailResponse> getCurrentMenusToSelect();

    public MenuResponse getMenuById(Integer menuId);

    public MenuResponse createMeal(CreateMealRequest request);

    public MenuResponse deleteMeal(DeleteMealRequest request);

    public void createOrder(CreateOrderRequest request);

    public MealResponse getMealForToday(JwtRequest request);


    public void createTestData();
}
