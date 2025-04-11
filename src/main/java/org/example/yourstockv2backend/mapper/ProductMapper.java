package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ProductDTO;
import org.example.yourstockv2backend.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ProductMaterialMapper.class})
public interface ProductMapper {

    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO productDTO);
}