package org.example.yourstockv2backend.service;

import lombok.RequiredArgsConstructor;
import org.example.yourstockv2backend.dto.CustomerDTO;
import org.example.yourstockv2backend.dto.SupplierDTO;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.mapper.CustomerMapper;
import org.example.yourstockv2backend.model.Customer;
import org.example.yourstockv2backend.model.Supplier;
import org.example.yourstockv2backend.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toDTO(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        Customer updatedCustomer = customerMapper.toEntity(customerDTO);
        updatedCustomer.setId(existingCustomer.getId());
        updatedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}