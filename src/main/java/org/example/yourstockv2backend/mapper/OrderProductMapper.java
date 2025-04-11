package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.OrderProductDTO;
import org.example.yourstockv2backend.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderProductMapper {

    @Mapping(source = "order.id", target = "orderId")
    OrderProductDTO toDto(OrderProduct orderProduct);

    @Mapping(source = "orderId", target = "order.id")
    OrderProduct toEntity(OrderProductDTO orderProductDTO);
}