package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.SaturdayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaturdayMealRepository extends JpaRepository<SaturdayMeal, Integer> {
    List<SaturdayMeal> findAllByMenu(Menu menu);
}
