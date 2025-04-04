package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.SupplierDTO;
import org.example.yourstockv2backend.exception.CustomException;
import org.example.yourstockv2backend.model.Supplier;
import org.example.yourstockv2backend.model.enums.Status;
import org.example.yourstockv2backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Transactional
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setStatus(supplierDTO.getStatus() != null ? supplierDTO.getStatus() : Status.ACTIVE);

        supplier = supplierRepository.save(supplier);
        supplierDTO.setId(supplier.getId());
        return supplierDTO;
    }

    @Transactional(readOnly = true)
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new CustomException("Supplier not found", HttpStatus.NOT_FOUND));
        return convertToDTO(supplier);
    }

    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new CustomException("Supplier not found", HttpStatus.NOT_FOUND));

        supplier.setName(supplierDTO.getName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        if (supplierDTO.getStatus() != null) {
            supplier.setStatus(supplierDTO.getStatus());
        }

        supplierRepository.save(supplier);
        return supplierDTO;
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new CustomException("Supplier not found", HttpStatus.NOT_FOUND));
        supplierRepository.delete(supplier);
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplier.getId());
        supplierDTO.setName(supplier.getName());
        supplierDTO.setContactPerson(supplier.getContactPerson());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setPhone(supplier.getPhone());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setStatus(supplier.getStatus());
        return supplierDTO;
    }
}