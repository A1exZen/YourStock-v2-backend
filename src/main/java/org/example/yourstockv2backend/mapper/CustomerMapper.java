package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.CustomerDTO;
import org.example.yourstockv2backend.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO customerDTO);
}