package com.tastomecsko.cafeteria.exception.meal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MealNotFoundException extends RuntimeException {
    public MealNotFoundException(String message) { super(message); }
}
