package com.utn.graduates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.utn.graduates.model.ContactType;
import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.model.Specialty;
import com.utn.graduates.repository.GraduateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GraduateService {
    private final GraduateRepository graduateRepository;
    private final ContactTypeService contactTypeService;
    private final SpecialtyService specialtyService;
    private static final String DNI_REGEX = "\\d{8}";
    private static final String PHONE_REGEX = "\\d{8,11}";
    private static final String NAME_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GraduateService(GraduateRepository graduateRepository, ContactTypeService contactTypeService, SpecialtyService specialtyService) {
        this.graduateRepository = graduateRepository;
        this.contactTypeService = contactTypeService;
        this.specialtyService = specialtyService;
    }

    @Transactional
    public GraduateDTO save(final GraduateDTO graduateDTO) {
        try {
            this.validateSaveGraduate(graduateDTO);
            Specialty specialty = this.specialtyService.findSpecialty(graduateDTO.getSpecialty());
            ContactType contactType = this.contactTypeService.findContactType(graduateDTO.getContactType());
            Graduate graduate = objectMapper.convertValue(graduateDTO, Graduate.class);
            graduate.setContactType(contactType);
            graduate.setSpecialty(specialty);
            Graduate saved = this.graduateRepository.save(graduate);
            return this.convertToDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new GraduateException("El graduado esta repetido");
        } catch (Exception e) {
            throw new GraduateException("El graduado no fue guardado. " + e.getMessage());
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
                .orElseThrow(() -> new GraduateException(String.format("Graduado con id: %s no fue encontrado", id)));
    }

    @Transactional
    public GraduateDTO updateGraduate(final Long graduateId, final GraduateDTO graduateDTO) {
        try {
            Graduate graduate = this.graduateRepository.findById(graduateId)
                    .orElseThrow(() -> new GraduateException(String.format("Graduado con id: %s no fue encontrado", graduateId)));
            this.validateUpdateGraduate(graduateId, graduateDTO, graduate);
            ContactType contactType = this.contactTypeService.findContactType(graduateDTO.getContactType());
            Specialty specialty = this.specialtyService.findSpecialty(graduateDTO.getSpecialty());
            graduate.setContactType(contactType);
            graduate.setEmail(graduateDTO.getEmail());
            graduate.setPhone(graduateDTO.getPhone());
            graduate.setSpecialty(specialty);
            return this.convertToDTO(this.graduateRepository.save(graduate));
        } catch (Exception e) {
            throw new GraduateException("Graduado con id: " + graduateId + " no se actualizo. " + e.getMessage());
        }
    }

    public void validateSaveGraduate(final GraduateDTO graduateDTO) {
        Preconditions.checkNotNull(graduateDTO, "El graduado no puede ser nulo");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getFullname()), "El nombre completo no puede estar vacio");
        Preconditions.checkState(graduateDTO.getFullname().matches(NAME_REGEX), "El nombre completo debe contener solo letras");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getDni()), "El DNI no puede estar vacio");
        Preconditions.checkState(graduateDTO.getDni().matches(DNI_REGEX), "El DNI debe ser numerico y tener 8 digitos");
        Preconditions.checkNotNull(graduateDTO.getGenre(), "El genero no puede estar vacio");
        if (graduateDTO.getPhone() != null) {
            Preconditions.checkState(graduateDTO.getPhone().matches(PHONE_REGEX), "El telefono debe ser numerico y tener entre 8 a 11 digitos");
        }
        Preconditions.checkNotNull(graduateDTO.getEmail(), "El Email no puede estar vacio");
        Preconditions.checkState(graduateDTO.getEmail().matches(EMAIL_REGEX), "El formato de Email es invalido y debe tenminar con '.com'");
    }

    private void validateUpdateGraduate(final Long graduateId, final GraduateDTO graduateDTO, final Graduate graduate) {
        Preconditions.checkNotNull(graduateId, "El Id de graduado no puede ser nulo");
        Preconditions.checkNotNull(graduateDTO, "El graduado no puede ser nulo");
        Preconditions.checkState(!StringUtils.isEmpty(graduateDTO.getEmail()), "El Email no puede estar vacio");
        Preconditions.checkState(graduateDTO.getEmail().matches(EMAIL_REGEX), "El formato de Email es invalido y debe tenminar con '.com'");
        if (graduateDTO.getPhone() != null) {
            Preconditions.checkState(graduateDTO.getPhone().matches(PHONE_REGEX), "El telefono debe ser numerico y tener entre 8 a 11 digitos");
        }
    }

    public void delete(final Long graduateId) {
        try {
            Preconditions.checkNotNull(graduateId, "El Id de graduado no puede ser nulo");
            this.graduateRepository.deleteById(graduateId);
        } catch (Exception e) {
            throw new GraduateException("Graduado con id: " + graduateId + " no fue borrado. " + e.getMessage());
        }
    }

    public void saveAll(final Set<Graduate> graduates) {
        this.graduateRepository.saveAll(graduates);
    }

    public Set<String> findAllDni() {
        return this.graduateRepository.findAllDni();
    }
}
