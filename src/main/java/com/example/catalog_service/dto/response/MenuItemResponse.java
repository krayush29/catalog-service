package com.example.catalog_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuItemResponse {
    private Long menuItemId;
    private String name;
    private double price;
}
