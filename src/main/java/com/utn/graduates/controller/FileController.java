package com.utn.graduates.controller;

import com.utn.graduates.error.CustomResponse;
import com.utn.graduates.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<CustomResponse> importGraduatesFromCsv(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Trying to import graduates from csv file: {}", file.getName());
        CustomResponse customResponse =  fileService.importGraduatesFromCsv(file);
        return new ResponseEntity<>(customResponse, customResponse.getCode());
    }
}
