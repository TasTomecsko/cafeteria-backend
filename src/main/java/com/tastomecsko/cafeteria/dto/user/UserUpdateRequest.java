package com.tastomecsko.cafeteria.dto.user;

import com.tastomecsko.cafeteria.entities.enums.Role;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private String token;
    private String firstName;
    private String lastName;
    private String oldPassword;
    private String newPassword;
}
