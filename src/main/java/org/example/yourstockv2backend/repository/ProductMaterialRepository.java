package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.model.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {

    List<ProductMaterial> findByProduct(Product product);

    List<ProductMaterial> findByMaterial(Material material);

    void deleteByProduct(Product product);
}