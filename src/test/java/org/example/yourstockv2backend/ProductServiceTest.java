package org.example.yourstockv2backend;

import org.example.yourstockv2backend.dto.ProductDTO;
import org.example.yourstockv2backend.dto.ProductMaterialDTO;
import org.example.yourstockv2backend.mapper.ProductMapper;
import org.example.yourstockv2backend.mapper.ProductMaterialMapper;
import org.example.yourstockv2backend.model.Category;
import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.example.yourstockv2backend.repository.CategoryRepository;
import org.example.yourstockv2backend.repository.MaterialRepository;
import org.example.yourstockv2backend.repository.ProductMaterialRepository;
import org.example.yourstockv2backend.repository.ProductRepository;
import org.example.yourstockv2backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMaterialRepository productMaterialRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductMaterialMapper productMaterialMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private Material material;
    private ProductMaterial productMaterial;
    private ProductMaterialDTO productMaterialDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        material = new Material();
        material.setId(1L);
        material.setName("Copper");
        material.setQuantity(100);
        material.setPrice(5.0);
        material.setUnit("kg");

        productMaterial = new ProductMaterial();
        productMaterial.setId(1L);
        productMaterial.setMaterial(material);
        productMaterial.setQuantity(2.0);
        productMaterial.setUnit("kg");

        productMaterialDTO = new ProductMaterialDTO();
        productMaterialDTO.setMaterialId(1L);
        productMaterialDTO.setQuantity(2.0);
        productMaterialDTO.setUnit("kg");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setCategory(category);
        product.setQuantity(10);
        product.setPrice(1000.0);
        product.setUnit("pcs");
        product.setRequiredMaterials(Arrays.asList(productMaterial));

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Laptop");
        productDTO.setDescription("Gaming Laptop");
        productDTO.setCategoryId(1L);
        productDTO.setQuantity(10);
        productDTO.setPrice(1000.0);
        productDTO.setUnit("pcs");
        productDTO.setRequiredMaterials(Arrays.asList(productMaterialDTO));
    }

    @Test
    void testGetAllProducts_ReturnsFilteredList() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Mouse");
        product2.setQuantity(0);

        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size(), "Должен быть возвращён только один продукт с quantity > 0");
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDto(any(Product.class));
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(1);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testGetProductById_ThrowsException_WhenNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(1L);
        });
        assertEquals("Продукт с ID 1 не найден", exception.getMessage());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testCreateProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(materialRepository.findById(1)).thenReturn(Optional.of(material));
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDTO);
        when(productMaterialMapper.toEntity(productMaterialDTO)).thenReturn(productMaterial);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMaterialRepository.save(any(ProductMaterial.class))).thenReturn(productMaterial);
        when(materialRepository.save(any(Material.class))).thenReturn(material);

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(1000.0, result.getPrice());
        verify(categoryRepository, times(1)).findById(1L);
        verify(materialRepository, times(2)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(materialRepository, times(1)).save(any(Material.class));
    }
}