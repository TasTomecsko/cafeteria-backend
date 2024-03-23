package com.tastomecsko.cafeteria.dto.user;

import com.tastomecsko.cafeteria.entities.enums.Role;
import lombok.Data;

@Data
public class UserInfoResponse {

    private Role role;
}
