package com.example.catalog_service.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantTest {

    @Test
    void testRestaurantConstructorWithNameAndAddress() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address", null);

        assertThat(restaurant.getName()).isEqualTo("test_restaurant");
        assertThat(restaurant.getAddress()).isEqualTo("test_address");
        assertThat(restaurant.getMenuItems()).isEmpty();
    }

    @Test
    void testAddMenuItemToRestaurant() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address", null);
        MenuItem menuItem = new MenuItem("test_item", 9.99, restaurant);

        restaurant.getMenuItems().add(menuItem);

        assertThat(restaurant.getMenuItems()).contains(menuItem);
    }

    @Test
    void testRemoveMenuItemFormRestaurant() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address", null);
        MenuItem menuItem = new MenuItem("test_item", 9.99, restaurant);

        restaurant.getMenuItems().add(menuItem);
        restaurant.getMenuItems().remove(menuItem);

        assertThat(restaurant.getMenuItems()).doesNotContain(menuItem);
    }
}