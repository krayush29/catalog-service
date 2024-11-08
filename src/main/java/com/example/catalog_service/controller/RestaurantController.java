package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.MenuItemRequest;
import com.example.catalog_service.dto.request.RestaurantRequest;
import com.example.catalog_service.dto.response.MenuItemResponse;
import com.example.catalog_service.dto.response.RestaurantResponse;
import com.example.catalog_service.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurantById(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @PostMapping
    public RestaurantResponse createRestaurant(@RequestBody @Valid RestaurantRequest restaurantRequest) {
        return restaurantService.createRestaurant(restaurantRequest);
    }

    @PostMapping("/{restaurantId}/menu-items")
    public MenuItemResponse createMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItemRequest menuItemRequest) {
        return restaurantService.createMenuItem(restaurantId, menuItemRequest);
    }

    @GetMapping("/{restaurantId}/menu-items")
    public List<MenuItemResponse> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        return restaurantService.getMenuItemsByRestaurantId(restaurantId);
    }
}
