package org.example.yourstockv2backend.repository;

import org.example.yourstockv2backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
