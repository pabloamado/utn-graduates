package com.utn.graduates.service;

import com.google.common.base.Preconditions;
import com.utn.graduates.dto.SpecialtyDTO;
import com.utn.graduates.exception.ContactTypeException;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.exception.SpecialtyException;
import com.utn.graduates.model.Specialty;
import com.utn.graduates.repository.SpecialtyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private static final String VALIDATION_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<String> getSpecialties() {
        return Optional.ofNullable(this.specialtyRepository.findAll())
                .map(list -> list.stream().map(contactType -> contactType.getName()).toList())
                .orElse(new ArrayList<>());
    }

    public SpecialtyDTO createSpecialty(final SpecialtyDTO specialtyDTO) {
        try {
            this.validateSpecialty(specialtyDTO.getName());
            Specialty saved = this.specialtyRepository.save(new Specialty(specialtyDTO.getName()));
            return new SpecialtyDTO(saved.getName());
        } catch (Exception e) {
            throw new SpecialtyException("Error creating specialty " + e.getMessage());
        }
    }

    public void deleteSpecialty(final String specialty) {
        try {
            this.validateSpecialty(specialty);
            this.specialtyRepository.deleteById(specialty);
        } catch (DataIntegrityViolationException e) {
            throw new GraduateException("Specialty is in use, can't be deleted");
        } catch (Exception e) {
            throw new ContactTypeException("Error deleting Specialty value " + e.getMessage());
        }
    }

    private void validateSpecialty(String specialty) {
        Preconditions.checkNotNull(specialty, "Specialty can't be null");
        if (!specialty.matches(VALIDATION_REGEX)) {
            throw new SpecialtyException("Specialty must contain only letters");
        }
    }

    public Specialty findSpecialty(final Specialty specialty) {
        Preconditions.checkNotNull(specialty, "Specialty can't be null");
        Preconditions.checkState(!StringUtils.isEmpty(specialty.getName()), "Specialty can't be empty");
        return this.specialtyRepository.findById(specialty.getName())
                .orElseThrow(() -> new ContactTypeException("Specialty doesn't exists, please create first the Specialty"));
    }
}
