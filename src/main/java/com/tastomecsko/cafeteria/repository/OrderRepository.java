package com.tastomecsko.cafeteria.repository;

import com.tastomecsko.cafeteria.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByAvailableFromBeforeAndAvailableToAfter(Date afterAvailableFrom, Date beforeAvailableTo);

    List<Order> findAllByAvailableFrom(Date availableFrom);
}
