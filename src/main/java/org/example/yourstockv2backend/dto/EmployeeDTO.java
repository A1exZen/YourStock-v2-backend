package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String position;
    private PersonalDetailDTO personalDetails;
}
