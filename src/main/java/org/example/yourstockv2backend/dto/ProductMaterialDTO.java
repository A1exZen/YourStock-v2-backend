package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductMaterialDTO {
    private Long id;
    private Long productId;
    private Long materialId;
    private String materialName;
    private BigDecimal quantity;
    private String unit;
}