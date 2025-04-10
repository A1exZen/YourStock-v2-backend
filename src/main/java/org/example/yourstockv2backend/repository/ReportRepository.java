package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.model.Product;
import org.example.yourstockv2backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByAction(Report.Action action);

    List<Report> findByEmployee(Employee employee);

    List<Report> findByProduct(Product product);

    List<Report> findByMaterial(Material material);

    List<Report> findByCreatedAtBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    List<Report> findByActionAndEmployee(Report.Action action, Employee employee);

    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.product LEFT JOIN FETCH r.material LEFT JOIN FETCH r.employee")
    List<Report> findAllWithDetails();
}