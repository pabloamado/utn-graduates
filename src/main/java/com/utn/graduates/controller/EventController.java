package com.utn.graduates.controller;

import com.utn.graduates.dto.EventDTO;
import com.utn.graduates.dto.TimeSlotDTO;
import com.utn.graduates.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.save(eventDTO);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<TimeSlotDTO> createTimeSlot(@PathVariable Long eventId, @RequestBody TimeSlotDTO timeSlotDTO) {
        TimeSlotDTO response = this.eventService.saveTimeSlot(eventId, timeSlotDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public EventDTO getEvent(@PathVariable Long id) {
        EventDTO eventDTO = eventService.getEvent(id);
        return eventDTO;
    }

    @GetMapping
    public Page<EventDTO> getEvents(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return eventService.getEvents(page, size);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Long eventId) {
        this.eventService.delete(eventId);
    }
}
