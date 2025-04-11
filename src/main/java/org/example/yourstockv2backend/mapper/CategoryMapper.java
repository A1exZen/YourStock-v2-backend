package org.example.yourstockv2backend.mapper;

import org.example.yourstockv2backend.dto.CategoryDTO;
import org.example.yourstockv2backend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", source = "createdAt")
    CategoryDTO toDto(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", source = "createdAt")
    Category toEntity(CategoryDTO categoryDTO);
}