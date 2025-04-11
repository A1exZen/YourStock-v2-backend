package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.Order;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private CustomerDTO customer;
    private EmployeeDTO employee;
    private Order.Status status;
    private String comment;
    private List<OrderProductDTO> orderProducts;
}