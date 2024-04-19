package com.tastomecsko.cafeteria.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadUserInformationUpdateException extends RuntimeException {

    private final String engMessage;
    private final String deMessage;
    private final String huMessage;

    public BadUserInformationUpdateException(String message, String messageDe, String messageHu) {
        super(message);
        engMessage = message;
        deMessage = messageDe;
        huMessage = messageHu;
    }
}
