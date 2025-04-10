package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.ProductDTO;
import org.example.yourstockv2backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMaterialMapper.class})
public interface ProductMapper {

//    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "category.name", target = "categoryName")
    ProductDTO toDTO(Product product);

//    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDTO productDTO);
}