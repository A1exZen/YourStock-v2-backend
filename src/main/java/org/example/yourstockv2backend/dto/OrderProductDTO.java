package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class OrderProductDTO {
    private Long id;
    private ProductDTO product;
    private String productName;
    private org.example.yourstockv2.dto.OrderDTO order;
    private Integer quantity;
    private Double priceAtOrder;
}