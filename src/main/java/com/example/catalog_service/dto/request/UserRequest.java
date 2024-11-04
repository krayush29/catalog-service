package com.example.catalog_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

    @NotNull(message = "Username cannot be null")
    @Size(min = 5, message = "Username should have at least 5 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;
}
