package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    UserDTO toDTO(User user);
    @Mapping(source = "employeeId", target = "employee.id")
    User toEntity(UserDTO userDTO);
}