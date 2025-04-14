// src/main/java/org/example/yourstockv2backend/controller/CategoryController.java

package org.example.yourstockv2backend.controller;

import org.example.yourstockv2backend.dto.CategoryDTO;
import org.example.yourstockv2backend.model.Category;
import org.example.yourstockv2backend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Запрос на получение всех категорий");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        logger.info("Возвращено {} категорий", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(@PathVariable String type) {
        logger.info("Запрос на получение категорий с типом: {}", type);
        Category.Type categoryType = Category.Type.valueOf(type.toUpperCase());
        List<CategoryDTO> categories = categoryService.getCategoriesByType(categoryType);
        logger.info("Возвращено {} категорий с типом {}", categories.size(), type);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("Запрос на создание категории: {}", categoryDTO.getName());
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        logger.info("Категория создана с ID: {}", createdCategory.getId());
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.info("Запрос на получение категории с ID: {}", id);
        CategoryDTO category = categoryService.getCategoryById(id);
        logger.info("Категория найдена: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("Запрос на обновление категории с ID: {}", id);
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        logger.info("Категория обновлена: {}", updatedCategory.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("Запрос на удаление категории с ID: {}", id);
        categoryService.deleteCategory(id);
        logger.info("Категория с ID {} удалена", id);
        return ResponseEntity.noContent().build();
    }
}