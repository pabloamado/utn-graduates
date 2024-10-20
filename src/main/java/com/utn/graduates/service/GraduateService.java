package com.utn.graduates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.repository.GraduateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraduateService {
    private final GraduateRepository graduateRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GraduateService(GraduateRepository graduateRepository) {
        this.graduateRepository = graduateRepository;
    }

    public List<GraduateDTO> getGraduates() {
        List<Graduate> graduates = this.graduateRepository.findAll();
        return this.convertToDTO(graduates);
    }

    public Page<GraduateDTO> getGraduatesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Graduate> graduatePage = graduateRepository.findAll(pageable);
        return graduatePage.map(graduate -> objectMapper.convertValue(graduate, GraduateDTO.class));
    }

    public Page<GraduateDTO> getByParam(String param, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Graduate> graduatePage = graduateRepository.findByParam(param, pageable);
        return graduatePage.map(g -> objectMapper.convertValue(g, GraduateDTO.class));
    }

    public List<GraduateDTO> convertToDTO(List<Graduate> graduates) {
        return graduates.stream().map(g -> convertToDTO(g)).collect(Collectors.toList());
    }

    public GraduateDTO convertToDTO(Graduate graduate) {
        return objectMapper.convertValue(graduate, GraduateDTO.class);
    }

    public Graduate getGraduateById(Long id) {
        return this.graduateRepository.findById(id)
                .orElseThrow(() -> new GraduateException(String.format("Graduate with id: %s not found", id)));
    }

    @Transactional
    public GraduateDTO updateGraduate(final Long graduateId, final GraduateDTO graduateDTO) {
        Graduate graduate = this.graduateRepository.findById(graduateId)
                .orElseThrow(() -> new GraduateException(String.format("Graduate with id: %s not found", graduateId)));
        if (graduate.getDni().equals(graduateDTO.getDni()) && graduate.getFullname().equals(graduateDTO.getFullname())) {
            graduate.setContactType(graduateDTO.getContactType());
            graduate.setSpecialty(graduateDTO.getSpecialty());
            return this.convertToDTO(this.graduateRepository.save(graduate));
        }
        throw new GraduateException("Graduate with id: " + graduateId + " not updated");
    }
}
