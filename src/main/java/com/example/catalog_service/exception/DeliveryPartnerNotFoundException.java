package com.example.catalog_service.exception;

public class DeliveryPartnerNotFoundException extends RuntimeException {
    public DeliveryPartnerNotFoundException(String message) {
        super(message);
    }
}
