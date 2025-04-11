package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.MaterialDTO;
import org.example.yourstockv2backend.mapper.MaterialMapper;
import org.example.yourstockv2backend.model.Material;
import org.example.yourstockv2backend.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    private final MaterialMapper materialMapper;

    public MaterialDTO createMaterial(MaterialDTO materialDTO) {
        Material material = materialMapper.toEntity(materialDTO);
        material = materialRepository.save(material);
        return materialMapper.toDto(material);
    }

    public MaterialDTO getMaterialById(Long id) {
        Material material = materialRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
        return materialMapper.toDto(material);
    }

    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
    }

    public MaterialDTO updateMaterial(Long id, MaterialDTO materialDTO) {
        Material existingMaterial = materialRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
        Material updatedMaterial = materialMapper.toEntity(materialDTO);
        updatedMaterial.setId(existingMaterial.getId());
        updatedMaterial = materialRepository.save(updatedMaterial);
        return materialMapper.toDto(updatedMaterial);
    }

    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Material not found with id: " + id);
        }
        materialRepository.deleteById(Math.toIntExact(id));
    }
}