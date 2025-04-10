package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.CustomerDTO;
import org.example.yourstockv2backend.dto.SupplierDTO;
import org.example.yourstockv2backend.model.Customer;
import org.example.yourstockv2backend.model.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierDTO toDTO(Supplier supplier);
    Supplier toEntity(SupplierDTO supplierDTO);
}