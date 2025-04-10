package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.dto.CustomerDTO;
import org.example.yourstockv2backend.dto.EmployeeDTO;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private CustomerDTO customer;
    private String customerName;
    private EmployeeDTO employee;
    private String employeeFirstName;
    private String employeeLastName;
    private String status;
    private String comment;
    private List<OrderProductDTO> orderProducts;
}