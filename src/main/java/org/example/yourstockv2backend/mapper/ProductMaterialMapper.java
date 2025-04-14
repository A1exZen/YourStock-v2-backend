package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ProductMaterialDTO;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MaterialMapper.class})
public interface ProductMaterialMapper {

    @Mapping(source = "material.id", target = "materialId")
    ProductMaterialDTO toDto(ProductMaterial productMaterial);

    @Mapping(source = "materialId", target = "material.id")
    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);
}