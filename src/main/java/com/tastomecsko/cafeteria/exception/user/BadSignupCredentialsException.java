package com.tastomecsko.cafeteria.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadSignupCredentialsException extends RuntimeException {

    public BadSignupCredentialsException(String message) {
        super(message);
    }
}
