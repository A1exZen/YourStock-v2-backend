package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findBySupplier(Supplier supplier);

    @Query("SELECT m FROM Material m LEFT JOIN FETCH m.category LEFT JOIN FETCH m.supplier WHERE m.id = :id")
    Material findByIdWithDetails(Long id);
}
