package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WarehouseTransactionDTO {
    private Long id;
    private String type;
    private ProductDTO product;
    private MaterialDTO material;
    private SupplierDTO supplier;
    private CustomerDTO customer;
    private Double quantity;
    private String unit;
    private OffsetDateTime createdAt;
    private EmployeeDTO employee;
}