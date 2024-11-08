package com.example.catalog_service.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MenuItemTest {

    @Test
    public void testMenuItemConstructor() {
        Restaurant restaurant = new Restaurant("test_restaurant", "test_address", null);
        MenuItem menuItem = new MenuItem("test_item", 9.99, restaurant);

        assertThat(menuItem.getName()).isEqualTo("test_item");
        assertThat(menuItem.getPrice()).isEqualTo(9.99);
        assertThat(menuItem.getRestaurant()).isEqualTo(restaurant);
    }
}