package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ProductMaterialDTO;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMaterialMapper {

//    @Mapping(source = "product.id", target = "productId")
//    @Mapping(source = "material.id", target = "materialId")
//    @Mapping(source = "material.name", target = "materialName")
    ProductMaterialDTO toDTO(ProductMaterial productMaterial);

//    @Mapping(source = "productId", target = "product.id")
//    @Mapping(source = "materialId", target = "material.id")
    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);
}