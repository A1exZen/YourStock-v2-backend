package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class ProductMaterialDTO {
    private Long id;
    private Long materialId;
    private Double quantity;
    private String unit;
}