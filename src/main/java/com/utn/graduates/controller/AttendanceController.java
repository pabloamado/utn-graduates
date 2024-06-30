package com.utn.graduates.controller;

import com.utn.graduates.dto.AttendanceDTO;
import com.utn.graduates.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attendance")
public class AttendanceController {
    private AttendanceService attendanceService;

    private AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PutMapping
    public ResponseEntity<AttendanceDTO> updatePresent(@RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO response = this.attendanceService.updatePresent(attendanceDTO);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
