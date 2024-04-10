package com.tastomecsko.cafeteria.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "user_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date availableFrom;
    private Date availableTo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "meals"))
    private List<Meal> meals;
}
