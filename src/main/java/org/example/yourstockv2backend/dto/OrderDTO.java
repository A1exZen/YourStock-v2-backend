package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.Order;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long customerId;
    private Long employeeId;
    private String status;
    private String comment;
    private Set<OrderProductDTO> orderProducts;
}