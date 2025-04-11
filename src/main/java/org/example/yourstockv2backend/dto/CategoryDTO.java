package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.Category;

import java.time.OffsetDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Category.Type type;
    private OffsetDateTime createdAt;
}