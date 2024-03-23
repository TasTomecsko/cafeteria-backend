package com.tastomecsko.cafeteria.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FinalAdminDeletionException extends RuntimeException{

    public FinalAdminDeletionException(String message) { super(message); }
}
