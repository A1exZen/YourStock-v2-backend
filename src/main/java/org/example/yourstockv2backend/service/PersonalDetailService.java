package org.example.yourstockv2backend.service;

import org.example.yourstockv2backend.dto.PersonalDetailDTO;
import org.example.yourstockv2backend.model.PersonalDetail;
import org.example.yourstockv2backend.repository.PersonalDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalDetailService {

    @Autowired
    private PersonalDetailRepository personalDetailRepository;

    public PersonalDetail createPersonalDetail(PersonalDetailDTO dto) {
        PersonalDetail personalDetail = new PersonalDetail();
        personalDetail.setPhoneNumber(dto.getPhoneNumber());
        personalDetail.setFirstName(dto.getFirstName());
        personalDetail.setLastName(dto.getLastName());
        personalDetail.setEmail(dto.getEmail());
        personalDetail.setCity(dto.getCity());
        return personalDetailRepository.save(personalDetail);
    }

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