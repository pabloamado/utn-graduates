package com.utn.graduates.service;

import com.google.common.base.Preconditions;
import com.utn.graduates.model.ContactType;
import com.utn.graduates.dto.ContactTypeDTO;
import com.utn.graduates.exception.ContactTypeException;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.repository.ContactTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactTypeService {

    private final ContactTypeRepository contactTypeRepository;
    private static final String VALIDATION_REGEX = "^[A-Z_]+$";

    public ContactTypeService(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    public ContactTypeDTO createContactType(ContactTypeDTO contactTypeDTO) {
        try {
            this.validateContactType(contactTypeDTO.getName());
            ContactType saved = this.contactTypeRepository.save(new ContactType(contactTypeDTO.getName()));
            return new ContactTypeDTO(saved.getName());
        } catch (Exception e) {
            throw new ContactTypeException("Error al crear tipo de contacto " + e.getMessage());
        }
    }

    public void deleteContactType(String contactType) {
        try {
            this.validateContactType(contactType);
            this.contactTypeRepository.deleteById(contactType);
        } catch (DataIntegrityViolationException e) {
            throw new GraduateException("El tipo de contacto esta en uso, no puede ser borrado.");
        } catch (Exception e) {
            throw new ContactTypeException("Error al borrar el tipo de contacto " + e.getMessage());
        }
    }

    private void validateContactType(String contactType) {
        Preconditions.checkNotNull(contactType, "Tipo de contacto no puede ser nulo");
        if (!contactType.matches(VALIDATION_REGEX)) {
            throw new ContactTypeException("El tipo de contacto debe contener solo letras en mayusculas y separado por guiones bajos.");
        }
    }

    public List<String> getContactTypes() {
        return Optional.ofNullable(this.contactTypeRepository.findAll())
                .map(list -> list.stream().map(contactType -> contactType.getName()).toList())
                .orElse(new ArrayList<>());
    }

    public ContactType findContactType(ContactType contactType) {
        Preconditions.checkNotNull(contactType, "El tipo de contacto no puede ser nulo");
        Preconditions.checkState(!StringUtils.isEmpty(contactType.getName()), "El tipo de contacto no puede estar vacio.");
        return this.contactTypeRepository.findById(contactType.getName())
                .orElseThrow(() -> new ContactTypeException("El tipo de contacto no existe, debes crearlo primero."));
    }
}
