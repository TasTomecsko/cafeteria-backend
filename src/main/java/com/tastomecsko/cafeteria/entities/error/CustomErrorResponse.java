package com.tastomecsko.cafeteria.entities.error;

import lombok.Data;

@Data
public class CustomErrorResponse {

    private String title;
    private Integer status;
    private String detail;
    private String message;
}
