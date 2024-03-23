package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.ThursdayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThursdayMealRepository extends JpaRepository<ThursdayMeal, Integer> {
    List<ThursdayMeal> findAllByMenu(Menu menu);
}
