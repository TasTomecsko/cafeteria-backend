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
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        Map<String, String> localizedTitle = new HashMap<>();
        Map<String, String> localizedMessage = new HashMap<>();

        localizedTitle.put("eng", "Forbidden");
        localizedTitle.put("de", "Verboten");
        localizedTitle.put("hu", "Illetéktelen");
        errorResponse.setTitle(localizedTitle);

        errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        errorResponse.setDetail(accessDeniedException.getMessage());

        localizedMessage.put("eng", "Unauthorized access");
        localizedMessage.put("de", "Unautorisierter Zugriff");
        localizedMessage.put("hu", "Illetéktelen hozzáférés");
        errorResponse.setMessage(localizedMessage);

        response.getWriter().write(objectMapper.writeValueAsString(
            errorResponse
        ));

    }
}
