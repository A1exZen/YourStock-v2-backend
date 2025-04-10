package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.EmployeeDTO;
import org.example.yourstockv2backend.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);
}