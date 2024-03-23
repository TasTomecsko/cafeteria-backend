package com.tastomecsko.cafeteria.exception.meal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadCreateMealRequestException extends RuntimeException {
    public BadCreateMealRequestException(String message) { super(message); }
}
