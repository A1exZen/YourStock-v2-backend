package org.example.yourstockv2backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialRequirementDTO {
    private double requiredQuantity;
    private double availableQuantity;
    private boolean isSufficient;
}