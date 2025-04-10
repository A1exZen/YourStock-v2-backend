package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Customer;
import org.example.yourstockv2backend.model.Supplier;
import org.example.yourstockv2backend.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
    List<Supplier> findByStatus(Status status);
    Optional<Supplier> findByName(String name);
}