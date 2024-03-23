package com.tastomecsko.cafeteria.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastomecsko.cafeteria.entities.error.CustomErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        CustomErrorResponse errorResponse = new CustomErrorResponse();

        errorResponse.setTitle("Forbidden");
        errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        errorResponse.setDetail(accessDeniedException.getMessage());
        errorResponse.setMessage("Unauthorized access");

        response.getWriter().write(objectMapper.writeValueAsString(
            errorResponse
        ));

    }
}
