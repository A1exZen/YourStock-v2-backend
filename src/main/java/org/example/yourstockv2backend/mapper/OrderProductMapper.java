package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.OrderProductDTO;
import org.example.yourstockv2backend.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "order.id", target = "orderId")
    OrderProductDTO toDto(OrderProduct orderProduct);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "orderId", target = "order.id")
    OrderProduct toEntity(OrderProductDTO orderProductDTO);
}