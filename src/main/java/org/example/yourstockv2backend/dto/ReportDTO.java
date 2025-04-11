package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.Report;

import java.time.OffsetDateTime;

@Data
public class ReportDTO {
    private Long id;
    private Report.Action action;
    private ProductDTO product;
    private MaterialDTO material;
    private String details;
    private OffsetDateTime createdAt;
    private EmployeeDTO employee;
}