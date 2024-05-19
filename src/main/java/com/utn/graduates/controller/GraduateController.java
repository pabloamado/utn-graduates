package com.utn.graduates.controller;

import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.service.GraduateService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<GraduateDTO> searchGraduatesByDniStartsWith(@RequestParam(required = false) String dni) {
        if (dni != null) {
            return graduateService.getByDni(dni);
        } else {
            return graduateService.getAll();
        }
    }

    @GetMapping("/search-fullname")
    public List<GraduateDTO> searchGraduatesByFullnameStartingWith(@RequestParam(required = false) String fullname) {
        if (!Strings.isBlank(fullname)) {
            return graduateService.getByFullname(fullname);
        } else {
            return graduateService.getAll();
        }
    }

}
