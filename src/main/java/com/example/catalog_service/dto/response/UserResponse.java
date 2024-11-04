package com.example.catalog_service.dto.response;

import com.example.catalog_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String username;
    private Role role;
}
