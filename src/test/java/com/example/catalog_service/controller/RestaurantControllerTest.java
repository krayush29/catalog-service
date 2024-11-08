package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.MenuItemRequest;
import com.example.catalog_service.dto.request.RestaurantRequest;
import com.example.catalog_service.dto.response.MenuItemResponse;
import com.example.catalog_service.dto.response.RestaurantResponse;
import com.example.catalog_service.entity.Location;
import com.example.catalog_service.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void testGetAllRestaurants() throws Exception {
        RestaurantResponse restaurantResponse = new RestaurantResponse(1L, "test_restaurant", "test_address", null, Collections.emptyList());

        //Mock response
        Mockito.when(restaurantService.getAllRestaurants()).thenReturn(Collections.singletonList(restaurantResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        RestaurantResponse restaurantResponse = new RestaurantResponse(1L, "test_restaurant", "test_address", null, Collections.emptyList());

        Mockito.when(restaurantService.getRestaurantById(1L)).thenReturn(restaurantResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateRestaurant() throws Exception {
        Location location = new Location(1.0, 1.0);
        RestaurantRequest restaurantRequest = new RestaurantRequest("username", "password", "test_restaurant", "test_address", location);
        String requestBody = objectMapper.writeValueAsString(restaurantRequest);

        RestaurantResponse restaurantResponse = new RestaurantResponse(1L, "test_restaurant", "test_address", null, Collections.emptyList());

        Mockito.when(restaurantService.createRestaurant(Mockito.any(RestaurantRequest.class))).thenReturn(restaurantResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        String requestBody = "{\"name\":\"test_item\",\"price\":9.99}";

        MenuItemResponse menuItemResponse = new MenuItemResponse(1L, "test_item", 9.99);

        Mockito.when(restaurantService.createMenuItem(Mockito.eq(1L), Mockito.any(MenuItemRequest.class))).thenReturn(menuItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetMenuItemsByRestaurantId() throws Exception {
        MenuItemResponse menuItemResponse = new MenuItemResponse(1L, "test_item", 9.99);

        Mockito.when(restaurantService.getMenuItemsByRestaurantId(1L)).thenReturn(Collections.singletonList(menuItemResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/menu-items")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}