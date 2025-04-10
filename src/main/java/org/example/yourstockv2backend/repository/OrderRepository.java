package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Customer;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByEmployee(Employee employee);
    List<Order> findByCreatedAtBetween(OffsetDateTime startDate, OffsetDateTime endDate);
    List<Order> findByCustomerAndStatus(Customer customer, Order.Status status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts op LEFT JOIN FETCH op.product WHERE o.id = :id")
    Order findByIdWithOrderProducts(Long id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.employee")
    List<Order> findAllWithDetails();
}