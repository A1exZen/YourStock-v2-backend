package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.EmployeeDTO;
import org.example.yourstockv2backend.model.Employee;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PersonalDetailService personalDetailService;

    @Transactional(readOnly = true)
    public Employee createEmployee(String position, PersonalDetail personalDetail) {
        Employee employee = new Employee();
        employee.setPosition(position);
        employee.setPersonalDetails(personalDetail);
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Employee employee, EmployeeDTO employeeDTO) {
        if (employeeDTO.getPosition() != null) {
            employee.setPosition(employeeDTO.getPosition());
        }
        if (employeeDTO.getPersonalDetails() != null) {
            personalDetailService.updatePersonalDetail(employee.getPersonalDetails(), employeeDTO.getPersonalDetails());
        }
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setPosition(employee.getPosition());
        dto.setPersonalDetails(personalDetailService.toDTO(employee.getPersonalDetails()));
        return dto;
    }
}