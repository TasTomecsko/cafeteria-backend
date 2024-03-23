package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.MondayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MondayMealRepository extends JpaRepository<MondayMeal, Integer> {
    List<MondayMeal> findAllByMenu(Menu menu);
}
