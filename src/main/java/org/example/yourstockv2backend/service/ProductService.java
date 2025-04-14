package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.MaterialRequirementDTO;
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
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMaterialRepository productMaterialRepository;
    private final MaterialRepository materialRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductMaterialMapper productMaterialMapper;

    private static final double MARKUP_PERCENTAGE = 0.2;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        logger.info("Получение списка всех продуктов");
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        logger.info("Получение продукта с ID: {}", id);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + id + " не найден"));
        Hibernate.initialize(product.getRequiredMaterials());
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        logger.info("Создание продукта с данными: {}", productDTO);
        validateProductDTO(productDTO);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категория с ID " + productDTO.getCategoryId() + " не найдена"));

        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        product.setRequiredMaterials(new ArrayList<>());

        if (productDTO.getRequiredMaterials() == null || productDTO.getRequiredMaterials().isEmpty()) {
            throw new IllegalArgumentException("Список необходимых материалов не может быть пустым");
        }

        Map<Long, Double> requiredQuantities = new HashMap<>();
        List<ProductMaterial> tempRequiredMaterials = new ArrayList<>();
        for (ProductMaterialDTO materialDTO : productDTO.getRequiredMaterials()) {
            validateProductMaterialDTO(materialDTO);
            Material material = materialRepository.findById(Math.toIntExact(materialDTO.getMaterialId()))
                    .orElseThrow(() -> new IllegalArgumentException("Материал с ID " + materialDTO.getMaterialId() + " не найден"));
            if (material.getPrice() == null) {
                throw new IllegalStateException("Цена материала с ID " + material.getId() + " не может быть null");
            }

            double totalRequiredQuantity = materialDTO.getQuantity() * productDTO.getQuantity();
            if (material.getQuantity() < totalRequiredQuantity) {
                throw new IllegalArgumentException(
                        "Недостаточно материала " + material.getName() + ": требуется " +
                                totalRequiredQuantity + " " + material.getUnit() + ", доступно " + material.getQuantity()
                );
            }
            requiredQuantities.put(material.getId(), totalRequiredQuantity);

            ProductMaterial tempProductMaterial = productMaterialMapper.toEntity(materialDTO);
            tempProductMaterial.setMaterial(material);
            tempRequiredMaterials.add(tempProductMaterial);
        }

        double totalCost = 0.0;
        for (ProductMaterial productMaterial : tempRequiredMaterials) {
            Material material = productMaterial.getMaterial();
            double materialCost = material.getPrice() * productMaterial.getQuantity();
            totalCost += materialCost;
        }
        double finalCost = totalCost * (1 + MARKUP_PERCENTAGE);
        logger.info("Итоговая стоимость продукта: {}", finalCost);
        product.setPrice(finalCost);

        product = productRepository.save(product);
        logger.info("Продукт сохранён с ID: {}", product.getId());

        for (ProductMaterialDTO materialDTO : productDTO.getRequiredMaterials()) {
            Material material = materialRepository.findById(Math.toIntExact(materialDTO.getMaterialId()))
                    .orElseThrow(() -> new IllegalArgumentException("Материал с ID " + materialDTO.getMaterialId() + " не найден"));

            double totalRequiredQuantity = requiredQuantities.get(material.getId());
            material.setQuantity((int) (material.getQuantity() - totalRequiredQuantity));
            materialRepository.save(material);
            logger.info("Материал с ID {}: использовано {} {}, осталось {}",
                    material.getId(), totalRequiredQuantity, material.getUnit(), material.getQuantity());

            ProductMaterial productMaterial = productMaterialMapper.toEntity(materialDTO);
            productMaterial.setProduct(product);
            productMaterial.setMaterial(material);

            productMaterial = productMaterialRepository.save(productMaterial);
            product.getRequiredMaterials().add(productMaterial);
        }

        return productMapper.toDto(product);
    }
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        logger.info("Обновление продукта с ID: {}, данные: {}", id, productDTO);

        validateProductDTO(productDTO);

        Product existingProduct = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + id + " не найден"));
        Hibernate.initialize(existingProduct.getRequiredMaterials());

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категория с ID " + productDTO.getCategoryId() + " не найдена"));

        if (productDTO.getRequiredMaterials() == null || productDTO.getRequiredMaterials().isEmpty()) {
            throw new IllegalArgumentException("Список необходимых материалов не может быть пустым");
        }

        int quantityDifference = productDTO.getQuantity() - existingProduct.getQuantity();
        Map<Long, Double> requiredQuantities = new HashMap<>();
        if (quantityDifference != 0) {
            for (ProductMaterialDTO materialDTO : productDTO.getRequiredMaterials()) {
                validateProductMaterialDTO(materialDTO);
                Material material = materialRepository.findById(Math.toIntExact(materialDTO.getMaterialId()))
                        .orElseThrow(() -> new IllegalArgumentException("Материал с ID " + materialDTO.getMaterialId() + " не найден"));
                if (material.getPrice() == null) {
                    throw new IllegalStateException("Цена материала с ID " + material.getId() + " не может быть null");
                }

                double totalRequiredQuantity = materialDTO.getQuantity() * quantityDifference;
                if (quantityDifference > 0) {
                    if (material.getQuantity() < totalRequiredQuantity) {
                        throw new IllegalArgumentException(
                                "Недостаточно материала " + material.getName() + ": требуется " +
                                        totalRequiredQuantity + " " + material.getUnit() + ", доступно " + material.getQuantity()
                        );
                    }
                }
                requiredQuantities.put(material.getId(), totalRequiredQuantity);
            }
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(category);
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setUnit(productDTO.getUnit());

        existingProduct.getRequiredMaterials().clear();

        List<ProductMaterial> tempRequiredMaterials = new ArrayList<>();
        for (ProductMaterialDTO materialDTO : productDTO.getRequiredMaterials()) {
            Material material = materialRepository.findById(Math.toIntExact(materialDTO.getMaterialId()))
                    .orElseThrow(() -> new IllegalArgumentException("Материал с ID " + materialDTO.getMaterialId() + " не найден"));
            ProductMaterial tempProductMaterial = productMaterialMapper.toEntity(materialDTO);
            tempProductMaterial.setMaterial(material);
            tempRequiredMaterials.add(tempProductMaterial);
        }

        existingProduct.setRequiredMaterials(tempRequiredMaterials);

        double totalCost = calculateProductCost(existingProduct);
        existingProduct.setPrice(totalCost);

        existingProduct = productRepository.save(existingProduct);

        existingProduct.getRequiredMaterials().clear();

        for (ProductMaterialDTO materialDTO : productDTO.getRequiredMaterials()) {
            Material material = materialRepository.findById(Math.toIntExact(materialDTO.getMaterialId()))
                    .orElseThrow(() -> new IllegalArgumentException("Материал с ID " + materialDTO.getMaterialId() + " не найден"));

            if (quantityDifference != 0) {
                double totalRequiredQuantity = requiredQuantities.get(material.getId());
                material.setQuantity((int) (material.getQuantity() - totalRequiredQuantity));
                materialRepository.save(material);
                logger.info("Материал с ID {}: использовано/возвращено {} {}, осталось {}",
                        material.getId(), totalRequiredQuantity, material.getUnit(), material.getQuantity());
            }

            ProductMaterial productMaterial = productMaterialMapper.toEntity(materialDTO);
            productMaterial.setProduct(existingProduct);
            productMaterial.setMaterial(material);
            productMaterial = productMaterialRepository.save(productMaterial);
            existingProduct.getRequiredMaterials().add(productMaterial);
        }

        logger.info("Продукт с ID {} обновлён, новая цена: {}", id, existingProduct.getPrice());
        return productMapper.toDto(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Удаление продукта с ID: {}", id);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + id + " не найден"));
        Hibernate.initialize(product.getRequiredMaterials());

//        for (ProductMaterial productMaterial : product.getRequiredMaterials()) {
//            Material material = productMaterial.getMaterial();
//            double totalReturnedQuantity = productMaterial.getQuantity() * product.getQuantity();
//            material.setQuantity((int) (material.getQuantity() + totalReturnedQuantity));
//            materialRepository.save(material);
//            logger.info("Материал с ID {}: возвращено {} {}, осталось {}",
//                    material.getId(), totalReturnedQuantity, material.getUnit(), material.getQuantity());
//        }

        productRepository.deleteById(Math.toIntExact(id));
    }

    private double calculateProductCost(Product product) {
        logger.info("Вычисление стоимости продукта с ID: {}", product.getId());
        double totalCost = 0.0;
        for (ProductMaterial productMaterial : product.getRequiredMaterials()) {
            Material material = productMaterial.getMaterial();
            if (material.getPrice() == null) {
                throw new IllegalStateException("Цена материала с ID " + material.getId() + " не может быть null");
            }
            double materialCost = material.getPrice() * productMaterial.getQuantity();
            logger.info("Материал ID {}: цена = {}, количество = {}, стоимость = {}",
                    material.getId(), material.getPrice(), productMaterial.getQuantity(), materialCost);
            totalCost += materialCost;
        }
        double finalCost = totalCost * (1 + MARKUP_PERCENTAGE);
        logger.info("Общая стоимость с наценкой ({}%): {}", MARKUP_PERCENTAGE * 100, finalCost);
        return finalCost;
    }

    @Transactional
    public void produceProduct(Long productId, int quantityToProduce) {
        logger.info("Производство продукта с ID: {}, количество: {}", productId, quantityToProduce);
        Product product = productRepository.findById(Math.toIntExact(productId))
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + productId + " не найден"));
        Hibernate.initialize(product.getRequiredMaterials());

        Map<Material, Double> requiredMaterials = new HashMap<>();
        for (ProductMaterial productMaterial : product.getRequiredMaterials()) {
            Material material = productMaterial.getMaterial();
            Hibernate.initialize(material);

            double requiredQuantity = productMaterial.getQuantity() * quantityToProduce;
            int availableQuantity = material.getQuantity();

            if (availableQuantity < requiredQuantity) {
                throw new RuntimeException(
                        "Недостаточно материала " + material.getName() + ": требуется " + requiredQuantity +
                                ", доступно " + availableQuantity
                );
            }

            requiredMaterials.put(material, requiredQuantity);
        }

        for (Map.Entry<Material, Double> entry : requiredMaterials.entrySet()) {
            Material material = entry.getKey();
            double usedQuantity = entry.getValue();
            material.setQuantity((int) (material.getQuantity() - usedQuantity));
            materialRepository.save(material);
            logger.info("Производство продукта '{}': использовано {} {} материала '{}'",
                    product.getName(), usedQuantity, material.getUnit(), material.getName());
        }

        product.setQuantity(product.getQuantity() + quantityToProduce);
        logger.info("Произведено {} единиц продукта '{}'. Новый запас: {}",
                quantityToProduce, product.getName(), product.getQuantity());

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Map<String, MaterialRequirementDTO> calculateMaterials(Long id, int quantity) {
        logger.info("Расчёт материалов для продукта с ID: {}, количество: {}", id, quantity);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        Hibernate.initialize(product.getRequiredMaterials());

        Map<String, MaterialRequirementDTO> requirements = new HashMap<>();
        for (ProductMaterial productMaterial : product.getRequiredMaterials()) {
            var material = productMaterial.getMaterial();
            Hibernate.initialize(material);

            double requiredQuantity = productMaterial.getQuantity() * quantity;
            double availableQuantity = material.getQuantity();
            boolean isSufficient = availableQuantity >= requiredQuantity;

            requirements.put(
                    material.getName(),
                    new MaterialRequirementDTO(requiredQuantity, availableQuantity, isSufficient)
            );
        }
        return requirements;
    }

    @Transactional(readOnly = true)
    public double calculateCost(Long id) {
        logger.info("Расчёт стоимости для продукта с ID: {}", id);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        Hibernate.initialize(product.getRequiredMaterials());

        double totalCost = 0.0;
        for (ProductMaterial productMaterial : product.getRequiredMaterials()) {
            var material = productMaterial.getMaterial();
            Hibernate.initialize(material);

            double materialCost = material.getPrice() * productMaterial.getQuantity();
            totalCost += materialCost;
        }
        return totalCost;
    }

    // Валидация ProductDTO
    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (productDTO.getCategoryId() == null || productDTO.getCategoryId() <= 0) {
            throw new IllegalArgumentException("ID категории должен быть указан и быть положительным");
        }
        if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Количество продукта должно быть неотрицательным");
        }
        if (productDTO.getUnit() == null || productDTO.getUnit().trim().isEmpty()) {
            throw new IllegalArgumentException("Единица измерения продукта не может быть пустой");
        }
    }

    // Валидация ProductMaterialDTO
    private void validateProductMaterialDTO(ProductMaterialDTO materialDTO) {
        if (materialDTO.getMaterialId() == null || materialDTO.getMaterialId() <= 0) {
            throw new IllegalArgumentException("ID материала должен быть указан и быть положительным");
        }
        if (materialDTO.getQuantity() == null || materialDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Количество материала должно быть положительным");
        }
        if (materialDTO.getUnit() == null || materialDTO.getUnit().trim().isEmpty()) {
            throw new IllegalArgumentException("Единица измерения материала не может быть пустой");
        }
    }
}