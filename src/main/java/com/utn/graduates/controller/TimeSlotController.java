package com.utn.graduates.controller;

import com.utn.graduates.dto.TimeSlotDTO;
import com.utn.graduates.service.TimeSlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PutMapping
    public ResponseEntity<TimeSlotDTO> update(@RequestBody TimeSlotDTO timeSlotDTO) {
        TimeSlotDTO response = timeSlotService.updateTimeSlot(timeSlotDTO);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity deleteTimeSlot(@PathVariable("timeSlotId") Long timeSlotId) {
        this.timeSlotService.delete(timeSlotId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
