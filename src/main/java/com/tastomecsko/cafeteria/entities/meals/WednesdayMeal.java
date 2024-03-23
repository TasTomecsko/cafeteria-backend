package com.tastomecsko.cafeteria.entities.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.DayOfWeek;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "wednesdayMeal")
public class WednesdayMeal extends BaseMeal {

}
