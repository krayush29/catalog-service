package com.example.catalog_service.dto.request;

import com.example.catalog_service.entity.Location;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantRequest {
    private String username;
    private String password;
    private String name;
    private String address;

    @NotNull(message = "Location cannot be null for restaurant")
    private Location location;
}
