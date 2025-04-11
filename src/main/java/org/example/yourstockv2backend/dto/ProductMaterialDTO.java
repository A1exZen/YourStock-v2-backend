package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class ProductMaterialDTO {
    private Long id;
    private Long productId;
    private MaterialDTO material;
    private Double quantity;
    private String unit;
}