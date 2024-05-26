package com.utn.graduates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.repository.GraduateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<GraduateDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Graduate> graduatePage = graduateRepository.findAll(pageable);
        Page<GraduateDTO> graduateDTOPage = graduatePage.map(graduate -> objectMapper.convertValue(graduate, GraduateDTO.class));
        return graduateDTOPage;
    }

    public Page<GraduateDTO> getByParam(String param, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Graduate> graduatePage = graduateRepository.findByParam(param, pageable);
        return graduatePage.map(g -> objectMapper.convertValue(g, GraduateDTO.class));
    }

    public GraduateDTO updateGraduate(final Long id, final GraduateDTO graduateDTO) {
        if (id == null || graduateDTO == null) {
            //TODO throw exception
        }
        Graduate graduate = graduateRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        return this.update(graduate, graduateDTO);
    }

    private GraduateDTO update(Graduate graduate, GraduateDTO graduateDTO) {
        graduate.setDescription(graduateDTO.getDescription());
        graduate.setKnown(graduateDTO.isKnown());
        Graduate save = graduateRepository.save(graduate);
        return this.objectMapper.convertValue(save, GraduateDTO.class);
    }

    private List<GraduateDTO> toGraduateDTO(List<Graduate> graduates) {
        return graduates.stream().map(g -> objectMapper.convertValue(g, GraduateDTO.class)).collect(Collectors.toList());
    }
}
