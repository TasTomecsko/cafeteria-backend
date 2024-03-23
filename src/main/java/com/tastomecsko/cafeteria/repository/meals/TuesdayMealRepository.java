package com.tastomecsko.cafeteria.repository.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.meals.TuesdayMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuesdayMealRepository extends JpaRepository<TuesdayMeal, Integer> {
    List<TuesdayMeal> findAllByMenu(Menu menu);
}
