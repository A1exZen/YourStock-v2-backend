package org.example.yourstockv2backend.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.mapper.PersonalDetailMapper;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.repository.PersonalDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalDetailService {


    private final PersonalDetailRepository personalDetailRepository;
    private final PersonalDetailMapper personalDetailMapper;

    public PersonalDetailDTO createPersonalDetail(PersonalDetailDTO personalDetailDTO) {
        PersonalDetail personalDetail = personalDetailMapper.toEntity(personalDetailDTO);
        personalDetail = personalDetailRepository.save(personalDetail);
        return personalDetailMapper.toDto(personalDetail);
    }

    public PersonalDetailDTO getPersonalDetailById(Long id) {
        PersonalDetail personalDetail = personalDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonalDetail not found with id: " + id));
        return personalDetailMapper.toDto(personalDetail);
    }

    public PersonalDetailDTO getPersonalDetailByEmail(String email) {
        PersonalDetail personalDetail = personalDetailRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("PersonalDetail not found with email: " + email));
        return personalDetailMapper.toDto(personalDetail);
    }

    public List<PersonalDetailDTO> getAllPersonalDetails() {
        return personalDetailRepository.findAll().stream()
                .map(personalDetailMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonalDetailDTO updatePersonalDetail(Long id, PersonalDetailDTO personalDetailDTO) {
        PersonalDetail existingPersonalDetail = personalDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonalDetail not found with id: " + id));
        PersonalDetail updatedPersonalDetail = personalDetailMapper.toEntity(personalDetailDTO);
        updatedPersonalDetail.setId(existingPersonalDetail.getId());
        updatedPersonalDetail = personalDetailRepository.save(updatedPersonalDetail);
        return personalDetailMapper.toDto(updatedPersonalDetail);
    }

    public void deletePersonalDetail(Long id) {
        if (!personalDetailRepository.existsById(id)) {
            throw new RuntimeException("PersonalDetail not found with id: " + id);
        }
        personalDetailRepository.deleteById(id);
    }

}