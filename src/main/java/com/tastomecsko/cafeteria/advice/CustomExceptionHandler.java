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

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleSecurityException(Exception exception) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();

        if(exception instanceof BadCredentialsException) {
            errorResponse.setTitle("Unauthorized");
            errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setDetail(exception.getMessage());
            errorResponse.setMessage("Authentication Failure!");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        if(exception instanceof SignatureException) {
            errorResponse.setTitle("Forbidden");
            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setDetail(exception.getMessage());
            errorResponse.setMessage("JWT signature not valid!");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        if(exception instanceof ExpiredJwtException) {
            errorResponse.setTitle("Forbidden");
            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setDetail(exception.getMessage());
            errorResponse.setMessage("JWT expired!");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        System.out.println("Unhandled Exception!");
        System.out.println(exception.getMessage());
        System.out.println(exception.getClass());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadSignupCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleSignupCredentialsException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Bad request");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Signup credential are invalid!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadUserInformationUpdateException.class)
    public ResponseEntity<CustomErrorResponse> handleUserInformationUpdateException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Failed to update user information");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Invalid information!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Resource not found");
        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("No user with provided id!");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUsernameNotFoundException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Resource not found");
        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("No user with provided email!");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FinalAdminDeletionException.class)
    public ResponseEntity<CustomErrorResponse> handleFinalAdminDeletionException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Admin deletion attempt");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Unable to delete final admin user!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Data integrity violation");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("The data you tried to add violated database constrains!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCreateMenuDetailsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCreateMenuDetailsException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Bad create menu request");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Menu creation failed due to some crucial information not being provided!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleMenuAlreadyExistException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Overlapping menu availability");
        errorResponse.setStatus(HttpServletResponse.SC_CONFLICT);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Menu creation failed due to the existence of another menu in the same timeframe!");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleMenuNotFoundException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Menu item not found");
        errorResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("Menu not found with the provided id!");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadModifyMenuDetailsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadModifyMenuDetailsException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Bad menu modification request");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("There was an error with the provided request details during modification!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCreateMealRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCreateMealRequestException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Bad meal creation request");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("There was an error with the provided details during meal creation!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadDeleteMealRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadDeleteMealRequestException(Exception exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Bad meal deletion request");
        errorResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setDetail(exception.getMessage());
        errorResponse.setMessage("There was an error with the provided details during meal deletion!");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
