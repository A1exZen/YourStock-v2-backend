package org.example.yourstockv2backend.controller;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.model.Order;
import org.example.yourstockv2backend.model.enums.Status;
import org.example.yourstockv2backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        logger.info("Получен запрос на обновление статуса заказа с ID: {}. Новый статус: {}", id, status);
        try {
            Order.Status orderStatus = Order.Status.valueOf(status);
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, orderStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            logger.error("Неверный статус заказа: {}", status, e);
            throw new IllegalArgumentException("Неверный статус заказа: " + status);
        }
    }
}