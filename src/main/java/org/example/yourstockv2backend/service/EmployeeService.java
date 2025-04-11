package org.example.yourstockv2backend.service;

import lombok.RequiredArgsConstructor;
import org.example.yourstockv2backend.dto.EmployeeDTO;
import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.mapper.EmployeeMapper;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;


    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByPersonalDetailsEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        Employee updatedEmployee = employeeMapper.toEntity(employeeDTO);
        updatedEmployee.setId(existingEmployee.getId());
        updatedEmployee = employeeRepository.save(updatedEmployee);
        return employeeMapper.toDto(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

}