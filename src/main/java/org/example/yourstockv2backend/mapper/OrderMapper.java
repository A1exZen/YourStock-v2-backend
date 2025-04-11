package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, OrderProductMapper.class})
public interface OrderMapper {

    OrderDTO toDto(Order order);
    Order toEntity(OrderDTO orderDTO);
}