package com.utn.graduates.service;

import com.utn.graduates.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }
}
