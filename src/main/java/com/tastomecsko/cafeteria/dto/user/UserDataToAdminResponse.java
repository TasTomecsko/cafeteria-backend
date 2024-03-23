package com.tastomecsko.cafeteria.dto.user;

import com.tastomecsko.cafeteria.entities.enums.Role;
import lombok.Data;

@Data
public class UserDataToAdminResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
