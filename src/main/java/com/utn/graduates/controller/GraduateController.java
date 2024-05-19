package com.utn.graduates.controller;

import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.service.GraduateService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graduate")
public class GraduateController {

    private final GraduateService graduateService;

    public GraduateController(GraduateService graduateService) {
        this.graduateService = graduateService;
    }

    @GetMapping("/search")
    public List<GraduateDTO> searchByDni(@RequestParam(required = false) String dni) {
        if (dni != null) {
            return graduateService.getByDni(dni);
        } else {
            return graduateService.getAll();
        }
    }

    @GetMapping("/search-fullname")
    public List<GraduateDTO> searchByFullname(@RequestParam(required = false) String fullname) {
        if (!Strings.isBlank(fullname)) {
            return graduateService.getByFullname(fullname);
        } else {
            return graduateService.getAll();
        }
    }

    @PutMapping("/{id}")
    public GraduateDTO updateGraduate(@PathVariable("id") Long id, GraduateDTO graduateDTO) {
        return this.graduateService.updateGraduate(id, graduateDTO);
    }

}
