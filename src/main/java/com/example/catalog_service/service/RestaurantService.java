package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.MenuItemRequest;
import com.example.catalog_service.dto.request.RestaurantRequest;
import com.example.catalog_service.dto.response.MenuItemResponse;
import com.example.catalog_service.dto.response.RestaurantResponse;
import com.example.catalog_service.entity.MenuItem;
import com.example.catalog_service.entity.Restaurant;
import com.example.catalog_service.exception.RestaurantNotFoundException;
import com.example.catalog_service.repository.MenuItemRepository;
import com.example.catalog_service.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(this::convertToRestaurantResponse).collect(Collectors.toList());
    }

    public Optional<RestaurantResponse> getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).map(this::convertToRestaurantResponse);
    }

    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        Restaurant restaurant = new Restaurant(restaurantRequest.getName(), restaurantRequest.getAddress());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToRestaurantResponse(savedRestaurant);
    }

    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest menuItemRequest) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + restaurantId));
        MenuItem menuItem = new MenuItem(menuItemRequest.getName(), menuItemRequest.getPrice(), restaurant);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return convertToMenuItemResponse(savedMenuItem);
    }

    public List<MenuItemResponse> getMenuItemsByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + restaurantId));
        return restaurant.getMenuItems().stream().map(this::convertToMenuItemResponse).collect(Collectors.toList());
    }

    private RestaurantResponse convertToRestaurantResponse(Restaurant restaurant) {
        List<MenuItemResponse> menuItemResponses = restaurant.getMenuItems().stream().map(this::convertToMenuItemResponse).collect(Collectors.toList());
        return new RestaurantResponse(restaurant.getName(), restaurant.getAddress(), menuItemResponses);
    }

    private MenuItemResponse convertToMenuItemResponse(MenuItem menuItem) {
        return new MenuItemResponse(menuItem.getName(), menuItem.getPrice());
    }
}
