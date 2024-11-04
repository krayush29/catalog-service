package com.example.catalog_service.dto.request;

import com.example.catalog_service.enums.Role;
import com.example.catalog_service.exception.InvalidRoleException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {

    @Setter
    @NotNull(message = "Username cannot be null")
    @Size(min = 5, message = "Username should have at least 5 characters")
    private String username;

    @Setter
    @NotNull(message = "Password cannot be null")
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;

    private Role role;

    public UserRegisterRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        isRoleValid(role);
        this.role = Role.valueOf(role);
    }

    public void setRole(String role) {
        isRoleValid(role);
        this.role = Role.valueOf(role);
    }

    private void isRoleValid(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return;
            }
        }
        throw new InvalidRoleException("Invalid user role: " + role + ". Please provide a valid user role." + Arrays.toString(Role.values()));
    }
}
