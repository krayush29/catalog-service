package com.example.catalog_service.service;

import com.example.catalog_service.dto.response.DeliveryPartnerResponse;
import com.example.catalog_service.entity.DeliveryPartner;
import com.example.catalog_service.entity.Location;
import com.example.catalog_service.entity.Restaurant;
import com.example.catalog_service.enums.FetchType;
import com.example.catalog_service.exception.RestaurantNotFoundException;
import com.example.catalog_service.repository.DeliveryPartnerRepository;
import com.example.catalog_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryPartnerServiceTest {

    @Mock
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private DeliveryPartnerService deliveryPartnerService;

    private Restaurant restaurant;
    private DeliveryPartner deliveryPartner1;
    private DeliveryPartner deliveryPartner2;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setLocation(new Location(10.0, 20.0));

        deliveryPartner1 = new DeliveryPartner();
        deliveryPartner1.setId(1L);
        deliveryPartner1.setLocation(new Location(11.0, 21.0));
        deliveryPartner1.setAvailable(true);

        deliveryPartner2 = new DeliveryPartner();
        deliveryPartner2.setId(2L);
        deliveryPartner2.setLocation(new Location(12.0, 22.0));
        deliveryPartner2.setAvailable(false);
    }

    @Test
    void testGetDeliveryPartnersWithAvailability() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(deliveryPartnerRepository.findByIsAvailable(true)).thenReturn(Arrays.asList(deliveryPartner1));

        List<DeliveryPartnerResponse> responses = deliveryPartnerService.getDeliveryPartners(1L, FetchType.ALL, true);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.getFirst().getUserId());
    }

    @Test
    void testGetDeliveryPartnersWithoutAvailability() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(deliveryPartnerRepository.findAll()).thenReturn(Arrays.asList(deliveryPartner1, deliveryPartner2));

        List<DeliveryPartnerResponse> responses = deliveryPartnerService.getDeliveryPartners(1L, FetchType.ALL, null);

        assertEquals(2, responses.size());
    }

    @Test
    void testGetDeliveryPartnersWithNearestFetchType() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(deliveryPartnerRepository.findAll()).thenReturn(Arrays.asList(deliveryPartner1, deliveryPartner2));

        List<DeliveryPartnerResponse> responses = deliveryPartnerService.getDeliveryPartners(1L, FetchType.NEAREST, null);

        assertEquals(2, responses.size());
        assertEquals(1L, responses.getFirst().getUserId());
    }

    @Test
    void testGetDeliveryPartnersRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> {
            deliveryPartnerService.getDeliveryPartners(1L, FetchType.ALL, null);
        });
    }

    @Test
    void testSortDeliveryPartnerByDistance() {
        List<DeliveryPartner> partners = Arrays.asList(deliveryPartner2, deliveryPartner1);
        List<DeliveryPartner> sortedPartners = deliveryPartnerService.sortDeliveryPartnerByDistance(restaurant, partners);

        assertEquals(1L, sortedPartners.get(0).getId());
        assertEquals(2L, sortedPartners.get(1).getId());
    }

    @Test
    void testSortDeliveryPartnerByDistanceWithSameLocation() {
        deliveryPartner2.setLocation(new Location(11.0, 21.0));
        List<DeliveryPartner> partners = Arrays.asList(deliveryPartner1, deliveryPartner2);
        List<DeliveryPartner> sortedPartners = deliveryPartnerService.sortDeliveryPartnerByDistance(restaurant, partners);

        assertEquals(1L, sortedPartners.get(0).getId());
        assertEquals(2L, sortedPartners.get(1).getId());
    }
}