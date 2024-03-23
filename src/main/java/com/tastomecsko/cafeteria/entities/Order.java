package com.tastomecsko.cafeteria.entities;

import com.tastomecsko.cafeteria.entities.meals.*;
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
    @JoinTable(joinColumns = @JoinColumn(name = "mondayMeal"))
    private List<MondayMeal> mondayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "tuesdayMeal"))
    private List<TuesdayMeal> tuesdayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "wednesdayMeal"))
    private List<WednesdayMeal> wednesdayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "thursdayMeal"))
    private List<ThursdayMeal> thursdayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "fridayMeal"))
    private List<FridayMeal> fridayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "saturdayMeal"))
    private List<SaturdayMeal> saturdayMeal;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST })
    @JoinTable(joinColumns = @JoinColumn(name = "sundayMeal"))
    private List<SundayMeal> sundayMeal;
}
