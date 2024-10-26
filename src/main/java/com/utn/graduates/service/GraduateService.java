package com.utn.graduates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.utn.graduates.constants.ContactType;
import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.repository.GraduateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final ContactTypeService contactTypeService;
    private static final int DNI_LENGTH = 8;
    private static final String PHONE_REGEX = "\\d{8,11}";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GraduateService(GraduateRepository graduateRepository, ContactTypeService contactTypeService) {
        this.graduateRepository = graduateRepository;
        this.contactTypeService = contactTypeService;
    }

    @Transactional
    public GraduateDTO save(final GraduateDTO graduateDTO) {
        try {
            this.validateSaveGraduate(graduateDTO);
            ContactType contactType = this.contactTypeService.findContactType(graduateDTO.getContactType().getValue());
            Graduate graduate = objectMapper.convertValue(graduateDTO, Graduate.class);
            graduate.setContactType(contactType);
            Graduate saved = this.graduateRepository.save(graduate);
            return this.convertToDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new GraduateException("Graduate is repeated");
        } catch (Exception e) {
            throw new GraduateException("Graduate not saved " + e.getMessage());
        }
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
        try {
            Graduate graduate = this.graduateRepository.findById(graduateId)
                    .orElseThrow(() -> new GraduateException(String.format("Graduate with id: %s not found", graduateId)));
            this.validateUpdateGraduate(graduateId, graduateDTO, graduate);
            ContactType contactType = this.contactTypeService.findContactType(graduateDTO.getContactType().getValue());
            graduate.setContactType(contactType);
            graduate.setEmail(graduateDTO.getEmail());
            graduate.setPhone(graduateDTO.getPhone());
            graduate.setGenre(graduateDTO.getGenre());
            graduate.setSpecialty(graduateDTO.getSpecialty());
            return this.convertToDTO(this.graduateRepository.save(graduate));
        } catch (Exception e) {
            throw new GraduateException("Graduate with id: " + graduateId + " not updated " + e.getMessage());
        }
    }

    private void validateSaveGraduate(final GraduateDTO graduateDTO) {
        Preconditions.checkNotNull(graduateDTO, "Graduate can't be null");
        Preconditions.checkNotNull(graduateDTO.getGenre(), "Genre must be present");
        Preconditions.checkNotNull(graduateDTO.getPhone(), "Phone must be present");
        Preconditions.checkState(graduateDTO.getPhone().matches(PHONE_REGEX), "Phone must be numeric and have between 8 and 11 digits");
        Preconditions.checkNotNull(graduateDTO.getEmail(), "Email must be present");
        Preconditions.checkState(graduateDTO.getEmail().matches(EMAIL_REGEX), "Email format is invalid and must end with '.com'");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getDni()), "Dni can't be null or empty");
        Preconditions.checkState(graduateDTO.getDni().length() == DNI_LENGTH, "Dni length must be 8, without dots");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getFullname()), "Fullname can't be null or empty");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getSpecialty()), "Specialty can't be null or empty");

    }

    private void validateUpdateGraduate(final Long graduateId, final GraduateDTO graduateDTO, final Graduate graduate) {
        Preconditions.checkNotNull(graduateDTO, "Graduate can't be null");
        Preconditions.checkNotNull(graduateId, "GraduateId can't be null");
        Preconditions.checkNotNull(graduateDTO.getGenre(), "Genre must be present");
        Preconditions.checkState(graduate.getDni().equals(graduateDTO.getDni()), "Dni must be the same");
        Preconditions.checkState(graduate.getFullname().equals(graduateDTO.getFullname()), "Fullname must be the same");
    }

}
