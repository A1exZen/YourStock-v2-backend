package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MaterialDTO {
    private Long id;
    private String name;
    private CategoryDTO category;
    private SupplierDTO supplier;
    private Double price;
    private Integer quantity;
    private String unit;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}