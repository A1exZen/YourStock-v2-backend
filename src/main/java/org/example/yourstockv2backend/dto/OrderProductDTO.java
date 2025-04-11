package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class OrderProductDTO {
    private Long id;
    private ProductDTO product;
    private Long orderId;
    private Integer quantity;
    private Double priceAtOrder;
}