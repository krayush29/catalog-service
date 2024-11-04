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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRestaurants() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address");
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(restaurant));

        List<RestaurantResponse> restaurantResponses = restaurantService.getAllRestaurants();

        assertThat(restaurantResponses).hasSize(1);
        assertThat(restaurantResponses.getFirst().getName()).isEqualTo("test_restaurant");
        assertThat(restaurantResponses.getFirst().getAddress()).isEqualTo("test_address");
    }

    @Test
    void testGetRestaurantById() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponse restaurantResponse = restaurantService.getRestaurantById(1L);

        assertThat(restaurantResponse.getName()).isEqualTo("test_restaurant");
        assertThat(restaurantResponse.getAddress()).isEqualTo("test_address");
    }

    @Test
    void testCreateRestaurant() {
        RestaurantRequest restaurantRequest = new RestaurantRequest("test_restaurant", "test_address");
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address");
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponse restaurantResponse = restaurantService.createRestaurant(restaurantRequest);

        assertThat(restaurantResponse.getName()).isEqualTo("test_restaurant");
        assertThat(restaurantResponse.getAddress()).isEqualTo("test_address");
    }

    @Test
    void testCreateMenuItem() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address");
        MenuItemRequest menuItemRequest = new MenuItemRequest("test_item", 9.99);
        MenuItem menuItem = new MenuItem("test_item", 9.99, restaurant);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItemResponse menuItemResponse = restaurantService.createMenuItem(1L, menuItemRequest);

        assertThat(menuItemResponse.getName()).isEqualTo("test_item");
        assertThat(menuItemResponse.getPrice()).isEqualTo(9.99);
    }

    @Test
    void testGetMenuItemsByRestaurantId() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address");
        MenuItem menuItem = new MenuItem("test_item", 9.99, restaurant);
        restaurant.getMenuItems().add(menuItem);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        List<MenuItemResponse> menuItemResponses = restaurantService.getMenuItemsByRestaurantId(1L);

        assertThat(menuItemResponses).hasSize(1);
        assertThat(menuItemResponses.getFirst().getName()).isEqualTo("test_item");
        assertThat(menuItemResponses.getFirst().getPrice()).isEqualTo(9.99);
    }

    @Test
    void testGetRestaurantByIdNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.getRestaurantById(1L));
    }

    @Test
    void testExceptionNotFoundForMenuItemWithInvalidRestaurant() {
        MenuItemRequest menuItemRequest = new MenuItemRequest("test_item", 9.99);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.createMenuItem(1L, menuItemRequest));
    }
}