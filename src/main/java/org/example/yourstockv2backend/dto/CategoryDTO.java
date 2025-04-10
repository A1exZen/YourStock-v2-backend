package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    private OffsetDateTime createdAt;
}