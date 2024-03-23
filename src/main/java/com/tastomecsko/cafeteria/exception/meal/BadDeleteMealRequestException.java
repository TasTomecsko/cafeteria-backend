package com.tastomecsko.cafeteria.exception.meal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadDeleteMealRequestException extends RuntimeException {
    public BadDeleteMealRequestException(String message) { super(message); }
}
