package com.utn.graduates.service;

import com.utn.graduates.dto.EventDTO;
import com.utn.graduates.exception.EventException;
import com.utn.graduates.model.Event;
import com.utn.graduates.model.TimeSlot;
import com.utn.graduates.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TimeSlotService timeSlotService;

    public EventService(EventRepository eventRepository, TimeSlotService timeSlotService) {
        this.eventRepository = eventRepository;
        this.timeSlotService = timeSlotService;
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = this.buildEvent(eventDTO);
        Event savedEvent = this.eventRepository.save(event);
        return this.convertToDTO(savedEvent);
    }

    public EventDTO getEvent(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventException(String.format("Event with id : %s  not found", id)));
        return convertToDTO(event);
    }

    public List<EventDTO> getEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private Event buildEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        List<TimeSlot> timeSlots = this.timeSlotService.toTimeSlots(eventDTO.getTimeSlots());
        timeSlots.forEach(ts -> ts.setEvent(event));
        event.setTimeSlots(timeSlots);
        return event;
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        eventDTO.setTimeSlots(event.getTimeSlots().stream().map(timeSlotService::convertToDTO).collect(Collectors.toList()));
        return eventDTO;
    }

}
