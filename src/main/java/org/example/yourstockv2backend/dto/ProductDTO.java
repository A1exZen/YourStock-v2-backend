package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private CategoryDTO category;
    private Integer quantity;
    private Double price;
    private String unit;
}