package com.tastomecsko.cafeteria.repository;

import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);
}
