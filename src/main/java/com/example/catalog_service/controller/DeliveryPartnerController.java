package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.DeliveryPartnerRequest;
import com.example.catalog_service.dto.response.DeliveryPartnerResponse;
import com.example.catalog_service.enums.FetchType;
import com.example.catalog_service.service.DeliveryPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/delivery-partners")
public class DeliveryPartnerController {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @GetMapping
    public List<DeliveryPartnerResponse> getDeliveryPartners(
            @RequestParam(value = "fetch-type", required = false) FetchType fetchType,
            @RequestParam(value = "availability", required = false) Boolean availability,
            @RequestBody DeliveryPartnerRequest request) {

        //TODO: verify the query params
        return deliveryPartnerService.getDeliveryPartners(request.getRestaurantId(), fetchType, availability);
    }

    @PutMapping("/{deliveryPartnerId}")
    public DeliveryPartnerResponse updateDeliveryPartnerLocation(@PathVariable Long deliveryPartnerId, @RequestBody DeliveryPartnerRequest request) {
        return deliveryPartnerService.updateDeliveryPartnerLocation(deliveryPartnerId, request);
    }
}
