package com.utn.graduates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.repository.GraduateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraduateService {

    private final GraduateRepository graduateRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GraduateService(GraduateRepository graduateRepository) {
        this.graduateRepository = graduateRepository;
    }

    public List<GraduateDTO> getAll() {
        List<Graduate> graduates = this.graduateRepository.findAll();
        return this.toGraduateDTO(graduates);
    }

    public List<GraduateDTO> getByFullname(final String fullname) {
        List<Graduate> graduates = this.graduateRepository.findByFullname(fullname);
        return this.toGraduateDTO(graduates);

    }

    public List<GraduateDTO> getByDni(final String dni) {
        List<Graduate> graduates = this.graduateRepository.findByDni(dni);
        if (graduates.size() > 1) {
            //TODO implementar preconditions para lanzar errores.
        }
        return this.toGraduateDTO(graduates);
    }

    private List<GraduateDTO> toGraduateDTO(List<Graduate> graduates) {
        return graduates.stream().map(g -> objectMapper.convertValue(g, GraduateDTO.class)).collect(Collectors.toList());
    }
}