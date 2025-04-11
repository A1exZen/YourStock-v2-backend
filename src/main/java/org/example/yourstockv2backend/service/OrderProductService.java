package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.OrderProductDTO;
import org.example.yourstockv2backend.mapper.OrderProductMapper;
import org.example.yourstockv2backend.model.OrderProduct;
import org.example.yourstockv2backend.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    private final OrderProductMapper orderProductMapper;

    public OrderProductDTO createOrderProduct(OrderProductDTO orderProductDTO) {
        OrderProduct orderProduct = orderProductMapper.toEntity(orderProductDTO);
        orderProduct = orderProductRepository.save(orderProduct);
        return orderProductMapper.toDto(orderProduct);
    }

    public OrderProductDTO getOrderProductById(Long id) {
        OrderProduct orderProduct = orderProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderProduct not found with id: " + id));
        return orderProductMapper.toDto(orderProduct);
    }

    public List<OrderProductDTO> getAllOrderProducts() {
        return orderProductRepository.findAll().stream()
                .map(orderProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderProductDTO updateOrderProduct(Long id, OrderProductDTO orderProductDTO) {
        OrderProduct existingOrderProduct = orderProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderProduct not found with id: " + id));
        OrderProduct updatedOrderProduct = orderProductMapper.toEntity(orderProductDTO);
        updatedOrderProduct.setId(existingOrderProduct.getId());
        updatedOrderProduct = orderProductRepository.save(updatedOrderProduct);
        return orderProductMapper.toDto(updatedOrderProduct);
    }

    public void deleteOrderProduct(Long id) {
        if (!orderProductRepository.existsById(id)) {
            throw new RuntimeException("OrderProduct not found with id: " + id);
        }
        orderProductRepository.deleteById(id);
    }
}