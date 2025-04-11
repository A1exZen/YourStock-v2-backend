package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.ProductMaterialDTO;
import org.example.yourstockv2backend.mapper.ProductMaterialMapper;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.example.yourstockv2backend.repository.ProductMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductMaterialService {

    private final ProductMaterialRepository productMaterialRepository;

    private final ProductMaterialMapper productMaterialMapper;

    public ProductMaterialDTO createProductMaterial(ProductMaterialDTO productMaterialDTO) {
        ProductMaterial productMaterial = productMaterialMapper.toEntity(productMaterialDTO);
        productMaterial = productMaterialRepository.save(productMaterial);
        return productMaterialMapper.toDto(productMaterial);
    }

    public ProductMaterialDTO getProductMaterialById(Long id) {
        ProductMaterial productMaterial = productMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductMaterial not found with id: " + id));
        return productMaterialMapper.toDto(productMaterial);
    }

    public List<ProductMaterialDTO> getAllProductMaterials() {
        return productMaterialRepository.findAll().stream()
                .map(productMaterialMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductMaterialDTO updateProductMaterial(Long id, ProductMaterialDTO productMaterialDTO) {
        ProductMaterial existingProductMaterial = productMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductMaterial not found with id: " + id));
        ProductMaterial updatedProductMaterial = productMaterialMapper.toEntity(productMaterialDTO);
        updatedProductMaterial.setId(existingProductMaterial.getId());
        updatedProductMaterial = productMaterialRepository.save(updatedProductMaterial);
        return productMaterialMapper.toDto(updatedProductMaterial);
    }

    public void deleteProductMaterial(Long id) {
        if (!productMaterialRepository.existsById(id)) {
            throw new RuntimeException("ProductMaterial not found with id: " + id);
        }
        productMaterialRepository.deleteById(id);
    }
}