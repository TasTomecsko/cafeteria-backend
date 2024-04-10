package com.tastomecsko.cafeteria.entities.comparator;

import com.tastomecsko.cafeteria.entities.Meal;

public class MealComparator implements java.util.Comparator<Meal> {
    @Override
    public int compare(Meal o1, Meal o2) {
        return (int) o1.getDateOfMeal().getTime() - (int) o2.getDateOfMeal().getTime();
    }
}
