package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.UserDTO;
import org.example.yourstockv2backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "role", source = "role")
    UserDTO toDto(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "role", source = "role")
    User toEntity(UserDTO userDTO);
}