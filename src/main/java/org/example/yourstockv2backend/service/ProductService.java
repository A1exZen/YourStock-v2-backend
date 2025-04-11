package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.ProductDTO;
import org.example.yourstockv2backend.mapper.ProductMapper;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        Product updatedProduct = productMapper.toEntity(productDTO);
        updatedProduct.setId(existingProduct.getId());
        updatedProduct = productRepository.save(updatedProduct);
        return productMapper.toDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(Math.toIntExact(id));
    }
}