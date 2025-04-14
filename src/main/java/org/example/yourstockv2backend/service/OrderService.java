package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.mapper.OrderMapper;
import org.example.yourstockv2backend.model.Order;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.repository.OrderRepository;
import org.example.yourstockv2backend.repository.ProductRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);

        for (var orderProductDTO : orderDTO.getOrderProducts()) {
            Product product = productRepository.findById(Math.toIntExact(orderProductDTO.getProductId()))
                    .orElseThrow(() -> new RuntimeException("Продукт с ID " + orderProductDTO.getProductId() + " не найден"));
            if (product.getQuantity() < orderProductDTO.getQuantity()) {
                throw new RuntimeException(
                        "Недостаточно продукта " + product.getName() + ": требуется " + orderProductDTO.getQuantity() +
                                ", доступно " + product.getQuantity()
                );
            }
            product.setQuantity(product.getQuantity() - orderProductDTO.getQuantity());
            orderProductDTO.setPriceAtOrder(product.getPrice());
        }

        order = orderRepository.save(order);
        Hibernate.initialize(order.getOrderProducts());
        Hibernate.initialize(order.getCustomer());
        Hibernate.initialize(order.getEmployee());
        return orderMapper.toDto(order);
    }
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
        Hibernate.initialize(order.getOrderProducts());
        Hibernate.initialize(order.getCustomer());
        Hibernate.initialize(order.getEmployee());
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    Hibernate.initialize(order.getOrderProducts());
                    Hibernate.initialize(order.getCustomer());
                    Hibernate.initialize(order.getEmployee());
                    return orderMapper.toDto(order);
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
        Order updatedOrder = orderMapper.toEntity(orderDTO);
        updatedOrder.setId(existingOrder.getId());

        for (var existingOrderProduct : existingOrder.getOrderProducts()) {
            Product product = productRepository.findById(Math.toIntExact(existingOrderProduct.getProduct().getId()))
                    .orElseThrow(() -> new RuntimeException("Продукт с ID " + existingOrderProduct.getProduct().getId() + " не найден"));
            product.setQuantity(product.getQuantity() + existingOrderProduct.getQuantity());
        }

        for (var orderProductDTO : orderDTO.getOrderProducts()) {
            Product product = productRepository.findById(Math.toIntExact(orderProductDTO.getProductId()))
                    .orElseThrow(() -> new RuntimeException("Продукт с ID " + orderProductDTO.getProductId() + " не найден"));
            if (product.getQuantity() < orderProductDTO.getQuantity()) {
                throw new RuntimeException(
                        "Недостаточно продукта " + product.getName() + ": требуется " + orderProductDTO.getQuantity() +
                                ", доступно " + product.getQuantity()
                );
            }
            product.setQuantity(product.getQuantity() - orderProductDTO.getQuantity());
            orderProductDTO.setPriceAtOrder(product.getPrice());
        }

        updatedOrder = orderRepository.save(updatedOrder);
        Hibernate.initialize(updatedOrder.getOrderProducts());
        Hibernate.initialize(updatedOrder.getCustomer());
        Hibernate.initialize(updatedOrder.getEmployee());
        return orderMapper.toDto(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
        Hibernate.initialize(order.getOrderProducts());

        for (var orderProduct : order.getOrderProducts()) {
            Product product = productRepository.findById(Math.toIntExact(orderProduct.getProduct().getId()))
                    .orElseThrow(() -> new RuntimeException("Продукт с ID " + orderProduct.getProduct().getId() + " не найден"));
            product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
        }

        orderRepository.deleteById(id);
    }


}