package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ReportDTO;
import org.example.yourstockv2backend.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, MaterialMapper.class, EmployeeMapper.class})
public interface ReportMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "material", source = "material")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "employee", source = "employee")
    ReportDTO toDto(Report report);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "material", source = "material")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "employee", source = "employee")
    Report toEntity(ReportDTO reportDTO);
}