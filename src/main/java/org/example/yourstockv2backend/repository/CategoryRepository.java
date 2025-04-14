package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    List<Category> findByType(Category.Type type);
    boolean existsByName(String name);
}