package com.tastomecsko.cafeteria.exception.menu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadModifyMenuDetailsException extends RuntimeException {

    public BadModifyMenuDetailsException(String message) {
        super(message);
    }
}
