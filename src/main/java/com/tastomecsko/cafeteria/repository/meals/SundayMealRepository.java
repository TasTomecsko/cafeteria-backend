package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.SundayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SundayMealRepository extends JpaRepository<SundayMeal, Integer> {
    List<SundayMeal> findAllByMenu(Menu menu);
}
