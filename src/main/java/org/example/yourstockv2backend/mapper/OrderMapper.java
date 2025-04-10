package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.OrderDTO;
import org.example.yourstockv2backend.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface OrderMapper {

//    @Mapping(source = "customer.id", target = "customerId")
//    @Mapping(source = "customer.name", target = "customerName")
//    @Mapping(source = "employee.id", target = "employeeId")
//    @Mapping(source = "employee.firstName", target = "employeeFirstName")
//    @Mapping(source = "employee.lastName", target = "employeeLastName")
    OrderDTO toDTO(Order order);

//    @Mapping(source = "customerId", target = "customer.id")
//    @Mapping(source = "employeeId", target = "employee.id")
    Order toEntity(OrderDTO orderDTO);
}