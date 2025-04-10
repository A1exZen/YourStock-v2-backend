package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private CategoryDTO category;
    private Integer quantity;
    private Double price;
    private String unit;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<ProductMaterialDTO> requiredMaterials;
}