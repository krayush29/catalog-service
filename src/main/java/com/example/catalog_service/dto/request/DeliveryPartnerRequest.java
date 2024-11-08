package com.example.catalog_service.dto.request;

import com.example.catalog_service.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerRequest {
    private Long restaurantId;
    private Boolean isAvailable;
    private Location location;
}
