package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.DeliveryPartnerRequest;
import com.example.catalog_service.dto.response.DeliveryPartnerResponse;
import com.example.catalog_service.entity.Location;
import com.example.catalog_service.enums.FetchType;
import com.example.catalog_service.enums.Role;
import com.example.catalog_service.service.DeliveryPartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryPartnerController.class)
class DeliveryPartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryPartnerService deliveryPartnerService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeliveryPartners() throws Exception {
        DeliveryPartnerResponse response = new DeliveryPartnerResponse(1L, "test_user", Role.DELIVERY_PARTNER, true, new Location(10.0, 20.0));
        when(deliveryPartnerService.getDeliveryPartners(any(), any(), any())).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/delivery-partners")
                        .param("fetch-type", FetchType.ALL.name())
                        .param("availability", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetDeliveryPartnersEmpty() throws Exception {
        when(deliveryPartnerService.getDeliveryPartners(any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/delivery-partners")
                        .param("fetch-type", FetchType.ALL.name())
                        .param("availability", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetDeliveryPartnersInvalidParams() throws Exception {
        mockMvc.perform(get("/delivery-partners")
                        .param("fetch-type", "INVALID")
                        .param("availability", "invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateDeliveryPartnerLocation() throws Exception {
        DeliveryPartnerRequest request = new DeliveryPartnerRequest(1L, true, new Location(10.0, 20.0));
        DeliveryPartnerResponse response = new DeliveryPartnerResponse(1L, "test_user", Role.DELIVERY_PARTNER, true, new Location(10.0, 20.0));
        String requestBody = objectMapper.writeValueAsString(request);
        String responseBody = objectMapper.writeValueAsString(response);

        when(deliveryPartnerService.updateDeliveryPartnerLocation(any(Long.class), any(DeliveryPartnerRequest.class))).thenReturn(response);

        mockMvc.perform(put("/delivery-partners/{deliveryPartnerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Test
    void testUpdateDeliveryPartnerLocationInvalid() throws Exception {
        mockMvc.perform(put("/delivery-partners/{deliveryPartnerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"restaurantId\": 1, \"location\": {\"latitude\": \"invalid\", \"longitude\": 20.0}}"))
                .andExpect(status().isBadRequest());
    }
}