package org.example.yourstockv2backend.repository;


import org.example.yourstockv2backend.model.Category;
import org.example.yourstockv2backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.requiredMaterials rm LEFT JOIN FETCH rm.material WHERE p.id = :id")
    Product findByIdWithRequiredMaterials(Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findAllWithCategory();
}
