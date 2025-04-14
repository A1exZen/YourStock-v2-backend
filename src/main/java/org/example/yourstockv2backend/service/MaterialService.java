package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.MaterialDTO;
import org.example.yourstockv2backend.mapper.MaterialMapper;
import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.repository.MaterialRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    private final MaterialMapper materialMapper;

    @Transactional
    public MaterialDTO createMaterial(MaterialDTO materialDTO) {
        Material material = materialMapper.toEntity(materialDTO);
        material = materialRepository.save(material);
        Hibernate.initialize(material.getCategory());
        Hibernate.initialize(material.getSupplier());
        return materialMapper.toDto(material);
    }

    @Transactional(readOnly = true)
    public MaterialDTO getMaterialById(Long id) {
        Material material = materialRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Материал с ID " + id + " не найден"));
        Hibernate.initialize(material.getCategory());
        Hibernate.initialize(material.getSupplier());
        return materialMapper.toDto(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .filter(material -> material.getQuantity() > 0)
                .map(material -> {
                    Hibernate.initialize(material.getCategory());
                    Hibernate.initialize(material.getSupplier());
                    return materialMapper.toDto(material);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialDTO updateMaterial(Long id, MaterialDTO materialDTO) {
        Material existingMaterial = materialRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Материал с ID " + id + " не найден"));
        Material updatedMaterial = materialMapper.toEntity(materialDTO);
        updatedMaterial.setId(existingMaterial.getId());
        updatedMaterial = materialRepository.save(updatedMaterial);
        Hibernate.initialize(updatedMaterial.getCategory());
        Hibernate.initialize(updatedMaterial.getSupplier());
        return materialMapper.toDto(updatedMaterial);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Материал с ID " + id + " не найден");
        }
        materialRepository.deleteById(Math.toIntExact(id));
    }
}