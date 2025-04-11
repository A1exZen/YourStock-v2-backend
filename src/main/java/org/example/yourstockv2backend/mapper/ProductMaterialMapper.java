package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ProductMaterialDTO;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, MaterialMapper.class})
public interface ProductMaterialMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductMaterialDTO toDto(ProductMaterial productMaterial);

    @Mapping(source = "productId", target = "product.id")
    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);
}