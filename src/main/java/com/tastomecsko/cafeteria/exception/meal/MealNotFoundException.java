package com.tastomecsko.cafeteria.exception.meal;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MealNotFoundException extends RuntimeException {

    private final String engMessage;
    private final String deMessage;
    private final String huMessage;

    public MealNotFoundException(String message, String messageDe, String messageHu) {
        super(message);
        engMessage = message;
        deMessage = messageDe;
        huMessage = messageHu;
    }
}
