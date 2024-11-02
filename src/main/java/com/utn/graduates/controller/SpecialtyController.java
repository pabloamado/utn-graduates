package com.utn.graduates.controller;

import com.utn.graduates.dto.SpecialtyDTO;
import com.utn.graduates.service.SpecialtyService;
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
@RequestMapping("/graduate/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public List<String> getSpecialties() {
        return this.specialtyService.getSpecialties();
    }

    @PostMapping
    public SpecialtyDTO createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        return this.specialtyService.createSpecialty(specialtyDTO);
    }

    @DeleteMapping("/{specialty}")
    public ResponseEntity deleteSpecialty(@PathVariable("specialty") String specialty) {
        this.specialtyService.deleteSpecialty(specialty);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
