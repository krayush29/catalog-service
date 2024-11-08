package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.DeliveryPartnerRequest;
import com.example.catalog_service.dto.response.DeliveryPartnerResponse;
import com.example.catalog_service.entity.DeliveryPartner;
import com.example.catalog_service.entity.Location;
import com.example.catalog_service.entity.Restaurant;
import com.example.catalog_service.enums.FetchType;
import com.example.catalog_service.exception.DeliveryPartnerNotFoundException;
import com.example.catalog_service.exception.RestaurantNotFoundException;
import com.example.catalog_service.repository.DeliveryPartnerRepository;
import com.example.catalog_service.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryPartnerService {

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<DeliveryPartnerResponse> getDeliveryPartners(Long restaurantId, FetchType fetchType, Boolean availability) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + restaurantId));

        // Fetch all  delivery partners by availability
        List<DeliveryPartner> availablePartners;

        if (availability != null) {
            availablePartners = deliveryPartnerRepository.findByIsAvailable(availability);
        } else {
            availablePartners = deliveryPartnerRepository.findAll();
        }

        if (FetchType.NEAREST.equals(fetchType)) {
            // Sort the delivery partners by distance from the restaurant
            availablePartners = sortDeliveryPartnerByDistance(restaurant, availablePartners);
        }
        return convertToDeliveryPartnerResponses(availablePartners);
    }

    public DeliveryPartnerResponse updateDeliveryPartnerLocation(Long deliveryPartnerId, DeliveryPartnerRequest request) {
        DeliveryPartner deliveryPartner = deliveryPartnerRepository.findById(deliveryPartnerId)
                .orElseThrow(() -> new DeliveryPartnerNotFoundException("Delivery Partner not found with id: " + deliveryPartnerId));

        if (request.getLocation() != null) deliveryPartner.setLocation(request.getLocation());
        if (request.getIsAvailable() != null) deliveryPartner.setAvailable(request.getIsAvailable());
        deliveryPartnerRepository.save(deliveryPartner);

        return new DeliveryPartnerResponse(
                deliveryPartner.getId(),
                deliveryPartner.getUsername(),
                deliveryPartner.getRole(),
                deliveryPartner.isAvailable(),
                deliveryPartner.getLocation()
        );
    }

    public List<DeliveryPartner> sortDeliveryPartnerByDistance(Restaurant restaurant, List<DeliveryPartner> availablePartners) {

        // Sort the delivery partners by distance from the restaurant
        availablePartners.sort((partner1, partner2) -> {
            double distance1 = calculateDistance(restaurant.getLocation(), partner1.getLocation());
            double distance2 = calculateDistance(restaurant.getLocation(), partner2.getLocation());
            return Double.compare(distance1, distance2);
        });

        return availablePartners;
    }

    private double calculateDistance(Location restaurantLocation, Location partnerLocation) {
        // Calculate the distance between the restaurant and the delivery partner
        return Math.sqrt(Math.pow(restaurantLocation.getLatitude() - partnerLocation.getLatitude(), 2) +
                Math.pow(restaurantLocation.getLongitude() - partnerLocation.getLongitude(), 2));
    }

    private List<DeliveryPartnerResponse> convertToDeliveryPartnerResponses(List<DeliveryPartner> partners) {
        return partners.stream()
                .map(partner -> new DeliveryPartnerResponse(
                        partner.getId(),
                        partner.getUsername(),
                        partner.getRole(),
                        partner.isAvailable(),
                        partner.getLocation()
                ))
                .collect(Collectors.toList());
    }
}
