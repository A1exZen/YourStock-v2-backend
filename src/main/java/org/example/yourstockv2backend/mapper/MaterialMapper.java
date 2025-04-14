package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.MaterialDTO;
import org.example.yourstockv2backend.model.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "supplier.id", target = "supplierId")
    MaterialDTO toDto(Material material);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "supplierId", target = "supplier.id")
    Material toEntity(MaterialDTO materialDTO);
}