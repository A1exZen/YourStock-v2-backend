package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.EmployeeDTO;
import org.example.yourstockv2backend.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonalDetailMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "personalDetails", source = "personalDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "personalDetails", source = "personalDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    Employee toEntity(EmployeeDTO employeeDTO);
}