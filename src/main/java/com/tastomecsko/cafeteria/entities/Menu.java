package com.tastomecsko.cafeteria.entities;

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
    private List<Meal> meals;
}
