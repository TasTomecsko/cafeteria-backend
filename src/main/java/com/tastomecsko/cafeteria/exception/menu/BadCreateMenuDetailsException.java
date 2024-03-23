package com.tastomecsko.cafeteria.exception.menu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadCreateMenuDetailsException extends RuntimeException {
    public BadCreateMenuDetailsException(String message) { super(message); }
}
