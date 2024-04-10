package com.tastomecsko.cafeteria.repository;

import com.tastomecsko.cafeteria.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findAllBySelectionStartBeforeAndSelectionEndAfter(Date beforeSelectionStart, Date afterSelectionEnd);

    List<Menu> findAllByAvailableFromAfterAndAvailableFromBeforeOrAvailableToAfterAndAvailableToBeforeOrAvailableFromOrAvailableToOrAvailableFromBeforeAndAvailableToAfter(
            Date afterSearchedBetweenAvailableFrom, Date beforeSearchedBetweenAvailableFrom,
            Date afterSearchedBetweenAvailableTo, Date beforeSearchedBetweenAvailableTo,
            Date searchedAvailableFrom,
            Date searchedAvailableTo,
            Date beforeSearchedAroundAvailableFrom, Date afterSearchedAroundAvailableTo
    );
}
