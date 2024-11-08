package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.MenuItemRequest;
import com.example.catalog_service.dto.request.RestaurantRequest;
import com.example.catalog_service.dto.response.MenuItemResponse;
import com.example.catalog_service.dto.response.RestaurantResponse;
import com.example.catalog_service.entity.MenuItem;
import com.example.catalog_service.entity.Restaurant;
import com.example.catalog_service.enums.Role;
import com.example.catalog_service.exception.RestaurantNotFoundException;
import com.example.catalog_service.repository.MenuItemRepository;
import com.example.catalog_service.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserService userService;

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(this::convertToRestaurantResponse).collect(Collectors.toList());
    }

    public RestaurantResponse getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + restaurantId));
        return convertToRestaurantResponse(restaurant);
    }

    // Only ADMIN can create a restaurant
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        userService.isAuthorize(restaurantRequest.getUsername(), restaurantRequest.getPassword(), Role.ADMIN);
        Restaurant restaurant = new Restaurant(restaurantRequest.getName(), restaurantRequest.getAddress(), restaurantRequest.getLocation());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToRestaurantResponse(savedRestaurant);
    }

    // Only ADMIN can create a menu item
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest menuItemRequest) {
        userService.isAuthorize(menuItemRequest.getUsername(), menuItemRequest.getPassword(), Role.ADMIN);
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
        return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getLocation(), menuItemResponses);
    }

    private MenuItemResponse convertToMenuItemResponse(MenuItem menuItem) {
        return new MenuItemResponse(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
    }
}
