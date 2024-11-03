package com.utn.graduates.controller;

import com.utn.graduates.dto.GraduateDTO;
import com.utn.graduates.service.GraduateService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graduate")
public class GraduateController {

    private final GraduateService graduateService;

    public GraduateController(GraduateService graduateService) {
        this.graduateService = graduateService;
    }

    /**
     * create a graduateDTO
     * @param graduateDTO
     * @return
     */
    @PostMapping
    public GraduateDTO createGraduate(@RequestBody GraduateDTO graduateDTO) {
        return this.graduateService.save(graduateDTO);
    }

    /**
     * search a graduateDTO by fullname or dni
     * @param param
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public Page<GraduateDTO> searchByParam(@RequestParam(required = false) String param,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        if (!Strings.isEmpty(param)) {
            return graduateService.getByParam(param, page, size);
        } else {
            return graduateService.getGraduatesPage(page, size);
        }
    }

    /**
     * update a graduateDTO
     * @return
     */
    @PutMapping("/{graduateId}")
    public GraduateDTO update(@PathVariable Long graduateId, @RequestBody GraduateDTO graduateDTO) {
        return this.graduateService.updateGraduate(graduateId, graduateDTO);
    }

    @DeleteMapping("/{graduateId}")
    public void delete(@PathVariable("graduateId") Long graduateId) {
        this.graduateService.delete(graduateId);
    }
}
