package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.mapper.OrderMapper;
import org.example.yourstockv2backend.model.Order;
import org.example.yourstockv2backend.model.OrderProduct;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.model.enums.Status;
import org.example.yourstockv2backend.repository.OrderRepository;
import org.example.yourstockv2backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Создание нового заказа");

        Order order = orderMapper.toEntity(orderDTO);
        order.setStatus(Order.Status.ACCEPTED);

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = productRepository.findById(Math.toIntExact(orderProduct.getProduct().getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + orderProduct.getProduct().getId() + " не найден"));

            if (product.getQuantity() < orderProduct.getQuantity()) {
                throw new IllegalArgumentException("Недостаточно продукта " + product.getName() + " на складе. Доступно: " + product.getQuantity());
            }

            product.setQuantity(product.getQuantity() - orderProduct.getQuantity());
            productRepository.save(product);

            orderProduct.setPriceAtOrder(product.getPrice());
            orderProduct.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        logger.info("Получение списка всех заказов");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, Order.Status newStatus) {
        logger.info("Обновление статуса заказа с ID: {} на {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));

        if (order.getStatus() == Order.Status.CANCELLED || order.getStatus() == Order.Status.DELIVERED) {
            throw new IllegalStateException("Нельзя изменить статус завершённого или отменённого заказа");
        }

        if (newStatus == Order.Status.CANCELLED) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = productRepository.findById(Math.toIntExact(orderProduct.getProduct().getId()))
                        .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + orderProduct.getProduct().getId() + " не найден"));
                product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }

}