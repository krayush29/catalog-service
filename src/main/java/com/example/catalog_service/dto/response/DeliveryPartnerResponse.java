package com.example.catalog_service.dto.response;

import com.example.catalog_service.entity.Location;
import com.example.catalog_service.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryPartnerResponse extends UserResponse {
    private boolean isAvailable;
    private Location location;

    public DeliveryPartnerResponse(Long userId, String username, Role role, Boolean isAvailable, Location location) {
        super(userId, username, role);
        this.isAvailable = isAvailable;
        this.location = location;
    }
}
