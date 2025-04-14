package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.dto.OrderProductDTO;
import org.example.yourstockv2backend.model.Order;
import org.example.yourstockv2backend.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "status", target = "status")
    OrderDTO toDto(Order order);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "status", target = "status")
    Order toEntity(OrderDTO orderDTO);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "order.id", target = "orderId")
    OrderProductDTO toDto(OrderProduct orderProduct);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "orderId", target = "order.id")
    OrderProduct toEntity(OrderProductDTO orderProductDTO);
}