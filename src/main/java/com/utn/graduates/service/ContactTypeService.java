package com.utn.graduates.service;

import com.google.common.base.Preconditions;
import com.utn.graduates.constants.ContactType;
import com.utn.graduates.dto.ContactTypeDTO;
import com.utn.graduates.exception.ContactTypeException;
import com.utn.graduates.exception.GraduateException;
import com.utn.graduates.repository.ContactTypeRepository;
import org.apache.commons.lang3.StringUtils;
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
            this.validateContactType(contactTypeDTO.getValue());
            this.contactTypeRepository.save(new ContactType(contactTypeDTO.getValue()));

        } catch (Exception e) {
            throw new ContactTypeException("Error creating contact type " + e.getMessage());
        }
        return contactTypeDTO;
    }

    public void deleteContactType(String contactType) {
        try {
            this.validateContactType(contactType);
            this.contactTypeRepository.deleteById(contactType);
        } catch (Exception e) {
            throw new ContactTypeException("Error deleting contact type " + e.getMessage());
        }
    }

    private void validateContactType(String contactType) {
        Preconditions.checkNotNull(contactType, "Contact type can't be null");
        if (!contactType.matches(VALIDATION_REGEX)) {
            throw new ContactTypeException("Contact type must contain only uppercase letters and underscores.");
        }
    }

    public List<String> getContactTypes() {
        return Optional.ofNullable(this.contactTypeRepository.findAll())
                .map(list -> list.stream().map(contactType -> contactType.getValue()).toList())
                .orElse(new ArrayList<>());
    }

    public ContactType findContactType(String contactType) {
        Preconditions.checkState(!StringUtils.isEmpty(contactType), "Contact type can't be null or empty");
        return this.contactTypeRepository.findById(contactType)
                .orElseThrow(() -> new ContactTypeException("ContactType doesn't exists, please create first the contactType"));
    }
}
