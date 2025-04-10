package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ReportDTO {
    private Long id;
    private String action;
    private ProductDTO product;
    private String productName;
    private MaterialDTO material;
    private String materialName;
    private String details;
    private OffsetDateTime createdAt;
    private EmployeeDTO employee;
    private String employeeFirstName;
    private String employeeLastName;
}