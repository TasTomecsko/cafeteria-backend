package com.tastomecsko.cafeteria.entities;

import com.tastomecsko.cafeteria.entities.meals.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date selectionStart;
    private Date selectionEnd;

    private Date availableFrom;
    private Date availableTo;

    @OneToMany(mappedBy = "menu")
    private List<MondayMeal> mondayMeals;

    @OneToMany(mappedBy = "menu")
    private List<TuesdayMeal> tuesdayMeals;

    @OneToMany(mappedBy = "menu")
    private List<WednesdayMeal> wednesdayMeals;

    @OneToMany(mappedBy = "menu")
    private List<ThursdayMeal> thursdayMeals;

    @OneToMany(mappedBy = "menu")
    private List<FridayMeal> fridayMeals;

    @OneToMany(mappedBy = "menu")
    private List<SaturdayMeal> saturdayMeals;

    @OneToMany(mappedBy = "menu")
    private List<SundayMeal> sundayMeals;
}
