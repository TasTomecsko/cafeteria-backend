package com.tastomecsko.cafeteria.repository;

import com.tastomecsko.cafeteria.entities.Meal;
import com.tastomecsko.cafeteria.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    List<Meal> findAllByMenu(Menu menu);
}
