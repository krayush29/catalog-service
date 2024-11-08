package com.example.catalog_service.repository;

import com.example.catalog_service.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    List<DeliveryPartner> findByIsAvailable(boolean available);
}
