package com.tastomecsko.cafeteria.entities.error;

import lombok.Data;

import java.util.Map;

@Data
public class CustomErrorResponse {

    private Map<String, String> title;
    private Integer status;
    private String detail;
    private Map<String, String> message;
}
