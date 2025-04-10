package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Order;
import org.example.yourstockv2backend.model.OrderProduct;
import org.example.yourstockv2backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrder(Order order);

    List<OrderProduct> findByProduct(Product product);

    void deleteByOrder(Order order);
}