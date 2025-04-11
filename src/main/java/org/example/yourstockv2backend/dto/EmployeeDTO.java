package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EmployeeDTO {
    private Long id;
    private String position;
    private PersonalDetailDTO personalDetails;
    private OffsetDateTime createdAt;
}