package com.example.catalog_service.entity;

import com.example.catalog_service.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "delivery-partners")
public class DeliveryPartner extends User {

    @Column(name = "is_available")
    private boolean isAvailable;

    @Embedded
    private Location location;

    public DeliveryPartner(String username, String password) {
        super(username, password, Role.DELIVERY_PARTNER);
        this.isAvailable = true;
        this.location = null;
    }
}
