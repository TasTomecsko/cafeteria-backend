package com.tastomecsko.cafeteria.repository;

import com.tastomecsko.cafeteria.entities.Order;
import com.tastomecsko.cafeteria.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByAvailableFromBeforeAndAvailableToAfterAndUser(Date afterAvailableFrom, Date beforeAvailableTo, User user);

    List<Order> findAllByAvailableFrom(Date availableFrom);
}
