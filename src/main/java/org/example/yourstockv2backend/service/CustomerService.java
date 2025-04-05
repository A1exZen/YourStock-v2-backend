package org.example.yourstockv2backend.service;

import lombok.RequiredArgsConstructor;
import org.example.yourstockv2backend.dto.CustomerDTO;
import org.example.yourstockv2backend.dto.SupplierDTO;
import org.example.yourstockv2backend.exception.CustomException;
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

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByName(customerDTO.getName())) {
            throw new IllegalArgumentException("Customer with this name already exists");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setStatus(customerDTO.getStatus());

        customer = customerRepository.save(customer);
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setContactPerson(customer.getContactPerson());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus());
        return dto;
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        return convertToDTO(customer);
    }


    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));

        if (!customer.getName().equals(customerDTO.getName()) && customerRepository.existsByName(customerDTO.getName())) {
            throw new IllegalArgumentException("Customer with this name already exists");
        }

        customer.setName(customerDTO.getName());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        if (customerDTO.getStatus() != null) {
            customer.setStatus(customerDTO.getStatus());
        }

        customerRepository.save(customer);
        return customerDTO;
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }
}