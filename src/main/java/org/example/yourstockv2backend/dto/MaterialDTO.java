package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class MaterialDTO {
    private Long id;
    private String name;
    private CategoryDTO category;
    private SupplierDTO supplier;
    private Double price;
    private Integer quantity;
    private Integer minimumQuantity;
    private String unit;
}