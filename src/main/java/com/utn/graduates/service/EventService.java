package com.utn.graduates.service;

import com.utn.graduates.model.Event;
import com.utn.graduates.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEvent(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Event with id : %s  not found", id)));

    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
}
