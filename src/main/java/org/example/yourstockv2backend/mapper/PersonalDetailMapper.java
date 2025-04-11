package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalDetailMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "createdAt", source = "createdAt")
    PersonalDetailDTO toDto(PersonalDetail personalDetail);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "createdAt", source = "createdAt")
    PersonalDetail toEntity(PersonalDetailDTO personalDetailDTO);
}