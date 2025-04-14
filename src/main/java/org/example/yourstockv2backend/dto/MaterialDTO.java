package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MaterialDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private Long supplierId;
    private Double price;
    private Integer quantity;
    private String unit;
}