package com.tastomecsko.cafeteria.advice;

import com.tastomecsko.cafeteria.entities.error.CustomErrorResponse;
import com.tastomecsko.cafeteria.exception.meal.BadCreateMealRequestException;
import com.tastomecsko.cafeteria.exception.meal.BadDeleteMealRequestException;
import com.tastomecsko.cafeteria.exception.menu.BadCreateMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.BadModifyMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.MenuAlreadyExistsException;
import com.tastomecsko.cafeteria.exception.menu.MenuNotFoundException;
import com.tastomecsko.cafeteria.exception.user.BadSignupCredentialsException;
import com.tastomecsko.cafeteria.exception.user.BadUserInformationUpdateException;
import com.tastomecsko.cafeteria.exception.user.FinalAdminDeletionException;
import com.tastomecsko.cafeteria.exception.user.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    Map<String,String> localizedTitle = new HashMap<>();
    Map<String, String> localizedMessage = new HashMap<>();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleSecurityException(Exception exception) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();

        if(exception instanceof BadCredentialsException) {
            localizedTitle.put("eng", "Unauthorized");
            localizedTitle.put("ge", "Nicht autorisiert");
            localizedTitle.put("hu", "Nem jogosult");
            errorResponse.setTitle(localizedTitle);

            errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setDetail(exception.getMessage());

            localizedMessage.put("eng", "Authentication Failure!");
            localizedMessage.put("ge", "Authentifizierungsfehler!");
            localizedMessage.put("hu", "Hitelesítési hiba!");
            errorResponse.setMessage(localizedMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        if(exception instanceof SignatureException) {
            localizedTitle.put("eng", "Forbidden");
            localizedTitle.put("ge", "Verboten");
            localizedTitle.put("hu", "Tiltott");
            errorResponse.setTitle(localizedTitle);

            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setDetail(exception.getMessage());

            localizedMessage.put("eng", "JWT signature not valid!");
            localizedMessage.put("ge", "JWT-Signatur ungültig!");
            localizedMessage.put("hu", "JWT eredetkompatibilitási hiba!");
            errorResponse.setMessage(localizedMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        if(exception instanceof ExpiredJwtException) {
            localizedTitle.put("eng", "Forbidden");
            localizedTitle.put("ge", "Verboten");
            localizedTitle.put("hu", "Tiltott");
            errorResponse.setTitle(localizedTitle);
            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setDetail(exception.getMessage());

            localizedMessage.put("eng", "JWT expired!");
            localizedMessage.put("ge", "JWT abgelaufen!");
            localizedMessage.put("hu", "A JWT érvényessége lejárt!");
            errorResponse.setMessage(localizedMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        System.out.println("Unhandled Exception!");
        System.out.println(exception.getMessage());
        System.out.println(exception.getClass());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadSignupCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleSignupCredentialsException(BadSignupCredentialsException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Bad request");
        localizedTitle.put("ge", "Ungültige Anforderung");
        localizedTitle.put("hu", "Hibás kérés");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadUserInformationUpdateException.class)
    public ResponseEntity<CustomErrorResponse> handleUserInformationUpdateException(BadUserInformationUpdateException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Failed to update user information");
        localizedTitle.put("ge", "Benutzerinformationen konnten nicht aktualisiert werden");
        localizedTitle.put("hu", "Nem sikerült frissíteni a felhasználói adatokat");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Resource not found");
        localizedTitle.put("ge", "Ressource nicht gefunden");
        localizedTitle.put("hu", "Nem található adat");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Resource not found");
        localizedTitle.put("ge", "Ressource nicht gefunden");
        localizedTitle.put("hu", "Nem található adat");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", "No user with provided email!");
        localizedMessage.put("ge", "Kein Benutzer mit angegebener E-Mail!");
        localizedMessage.put("hu", "Nincs felhasználó megadott e-mail címmel!");
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FinalAdminDeletionException.class)
    public ResponseEntity<CustomErrorResponse> handleFinalAdminDeletionException(FinalAdminDeletionException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Admin deletion attempt");
        localizedTitle.put("ge", "Löschversuch des Administrators");
        localizedTitle.put("hu", "Adminisztrátor törlési kísérlet");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Data integrity violation");
        localizedTitle.put("ge", "Verletzung der Datenintegrität");
        localizedTitle.put("hu", "Adatintegritás megsértése");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", "The data you tried to add violated database constrains!");
        localizedMessage.put("ge", "Die Daten, die Sie hinzufügen wollten, haben gegen Datenbankeinschränkungen verstoßen!");
        localizedMessage.put("hu", "Az adatok, amelyeket megpróbált hozzáadni, megsértették az adatbázis-korlátozásokat!");
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCreateMenuDetailsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCreateMenuDetailsException(BadCreateMenuDetailsException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Bad create menu request");
        localizedTitle.put("ge", "Fehlerhafte Menüerstellungsanfrage");
        localizedTitle.put("hu", "Hibás menü létrehozási kérelem");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleMenuAlreadyExistException(MenuAlreadyExistsException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Overlapping menu availability");
        localizedTitle.put("ge", "Überlappende Menüverfügbarkeit");
        localizedTitle.put("hu", "Átfedés a menük elérhetősége között");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_CONFLICT);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleMenuNotFoundException(MenuNotFoundException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Menu item not found");
        localizedTitle.put("ge", "Menüpunkt nicht gefunden");
        localizedTitle.put("hu", "A menüelem nem található");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadModifyMenuDetailsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadModifyMenuDetailsException(BadModifyMenuDetailsException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Bad menu modification request");
        localizedTitle.put("ge", "Ungültige Anfrage zur Menüänderung");
        localizedTitle.put("hu", "Hibás menümódosítási kérelem");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCreateMealRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCreateMealRequestException(BadCreateMealRequestException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Bad meal creation request");
        localizedTitle.put("ge", "Anfrage zur Zubereitung einer schlechten Mahlzeit");
        localizedTitle.put("hu", "Hibás étel létrehozási kérelem");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadDeleteMealRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadDeleteMealRequestException(BadDeleteMealRequestException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        localizedTitle.put("eng", "Bad meal deletion request");
        localizedTitle.put("ge", "Anfrage zum Löschen einer schlechten Mahlzeit");
        localizedTitle.put("hu", "Hibás étel törlési kérelem");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());

        localizedMessage.put("eng", exception.getEngMessage());
        localizedMessage.put("ge", exception.getDeMessage());
        localizedMessage.put("hu", exception.getHuMessage());
        errorResponse.setMessage(localizedMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
