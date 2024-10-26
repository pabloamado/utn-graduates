package com.utn.graduates.controller;

import com.utn.graduates.dto.ContactTypeDTO;
import com.utn.graduates.service.ContactTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graduate/contact_type")
public class ContactTypeController {

    private final ContactTypeService contactTypeService;

    public ContactTypeController(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @GetMapping
    public List<String> getContactTypes() {
        return this.contactTypeService.getContactTypes();
    }

    @PostMapping
    public ContactTypeDTO createContactType(@RequestBody ContactTypeDTO contactTypeDTO) {
        return this.contactTypeService.createContactType(contactTypeDTO);
    }

    @DeleteMapping("/{contactType}")
    public ResponseEntity deleteContactType(@PathVariable("contactType") String contactType) {
        this.contactTypeService.deleteContactType(contactType);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
