package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.WednesdayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WednesdayMealRepository extends JpaRepository<WednesdayMeal, Integer> {
    List<WednesdayMeal> findAllByMenu(Menu menu);
}
