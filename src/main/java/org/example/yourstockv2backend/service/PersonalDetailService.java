package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.repository.PersonalDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonalDetailService {

    @Autowired
    private PersonalDetailRepository personalDetailRepository;

    @Transactional(readOnly = true)
    public PersonalDetail createPersonalDetail(PersonalDetailDTO dto) {
        PersonalDetail personalDetail = new PersonalDetail();
        personalDetail.setPhoneNumber(dto.getPhoneNumber());
        personalDetail.setFirstName(dto.getFirstName());
        personalDetail.setLastName(dto.getLastName());
        personalDetail.setEmail(dto.getEmail());
        personalDetail.setCity(dto.getCity());
        return personalDetailRepository.save(personalDetail);
    }

    @Transactional
    public PersonalDetail updatePersonalDetail(PersonalDetail personalDetail, PersonalDetailDTO dto) {
        if (dto.getFirstName() != null) personalDetail.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) personalDetail.setLastName(dto.getLastName());
        if (dto.getEmail() != null) personalDetail.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) personalDetail.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getCity() != null) personalDetail.setCity(dto.getCity());
        return personalDetailRepository.save(personalDetail);
    }

    @Transactional(readOnly = true)
    public PersonalDetailDTO toDTO(PersonalDetail personalDetail) {
        PersonalDetailDTO dto = new PersonalDetailDTO();
        dto.setId(personalDetail.getId());
        dto.setPhoneNumber(personalDetail.getPhoneNumber());
        dto.setFirstName(personalDetail.getFirstName());
        dto.setLastName(personalDetail.getLastName());
        dto.setEmail(personalDetail.getEmail());
        dto.setCity(personalDetail.getCity());
        return dto;
    }
}