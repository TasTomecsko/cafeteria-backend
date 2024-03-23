package com.tastomecsko.cafeteria.entities.meals;

import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.Order;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String identification;
    private String description;

    @ManyToOne
    @JoinColumn(name = "menu")
    private Menu menu;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "user_order"))
    private List<Order> user_order;
}
