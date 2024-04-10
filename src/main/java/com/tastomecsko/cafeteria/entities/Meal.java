package com.tastomecsko.cafeteria.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String identification;
    private String description;
    private Date dateOfMeal;

    @ManyToOne
    @JoinColumn(name = "menu")
    private Menu menu;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "user_order"))
    private List<Order> user_order;
}
