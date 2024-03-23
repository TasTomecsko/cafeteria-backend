package com.tastomecsko.cafeteria.exception.menu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MenuAlreadyExistsException extends RuntimeException {

    public MenuAlreadyExistsException(String message) {super(message);}
}
