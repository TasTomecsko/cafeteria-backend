package com.tastomecsko.cafeteria.dto.security;

import lombok.Data;

@Data
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer role;
}
