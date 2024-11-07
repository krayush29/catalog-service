package com.example.catalog_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantRequest {
    private String username;
    private String password;
    private String name;
    private String address;
}
