package com.utn.graduates.controller;

import com.utn.graduates.dto.EventDTO;
import com.utn.graduates.model.Event;
import com.utn.graduates.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

    @GetMapping("/{id}")
    public EventDTO getEvent(@PathVariable Long id) {
        EventDTO eventDTO = eventService.getEvent(id);
        return eventDTO;
    }

    @GetMapping
    public List<EventDTO> getEvents() {
        return eventService.getEvents();
    }


}
