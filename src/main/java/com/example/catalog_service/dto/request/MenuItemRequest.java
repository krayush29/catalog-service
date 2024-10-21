package com.example.catalog_service.dto.request;

import lombok.Data;

@Data
public class MenuItemRequest {
    private String name;
    private double price;
}
