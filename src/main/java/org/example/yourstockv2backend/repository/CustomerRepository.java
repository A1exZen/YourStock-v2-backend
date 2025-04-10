package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Customer;
import org.example.yourstockv2backend.model.Supplier;
import org.example.yourstockv2backend.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByName(String name);
    List<Customer> findByStatus(Status status);
    Optional<Customer> findByName(String name);
}