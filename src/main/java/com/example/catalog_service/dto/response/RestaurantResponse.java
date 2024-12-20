package com.example.catalog_service.dto.response;

import com.example.catalog_service.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantResponse {
    private Long restaurantId;
    private String name;
    private String address;
    private Location location;
    private List<MenuItemResponse> menuItems;
}
