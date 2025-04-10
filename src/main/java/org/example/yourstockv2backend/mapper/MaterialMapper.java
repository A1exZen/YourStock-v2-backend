package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.MaterialDTO;
import org.example.yourstockv2backend.model.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

//    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "category.name", target = "categoryName")
//    @Mapping(source = "supplier.id", target = "supplierId")
//    @Mapping(source = "supplier.name", target = "supplierName")
    MaterialDTO toDTO(Material material);

//    @Mapping(source = "categoryId", target = "category.id")
//    @Mapping(source = "supplierId", target = "supplier.id")
    Material toEntity(MaterialDTO materialDTO);
}