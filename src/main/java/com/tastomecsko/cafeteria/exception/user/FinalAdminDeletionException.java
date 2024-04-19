package com.tastomecsko.cafeteria.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FinalAdminDeletionException extends RuntimeException {

    private final String engMessage;
    private final String deMessage;
    private final String huMessage;

    public FinalAdminDeletionException(String message, String messageDe, String messageHu) {
        super(message);
        engMessage = message;
        deMessage = messageDe;
        huMessage = messageHu;
    }
}
