package com.utn.graduates.service;

import com.utn.graduates.dto.EventDTO;
import com.utn.graduates.dto.TimeSlotDTO;
import com.utn.graduates.exception.EventException;
import com.utn.graduates.model.Event;
import com.utn.graduates.model.TimeSlot;
import com.utn.graduates.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TimeSlotService timeSlotService;

    public EventService(EventRepository eventRepository, TimeSlotService timeSlotService) {
        this.eventRepository = eventRepository;
        this.timeSlotService = timeSlotService;
    }

    /**
     * create an event with timeslots
     * @param eventDTO
     * @return
     */
    @Transactional
    public EventDTO save(EventDTO eventDTO) {
        Event event = this.buildEvent(eventDTO);
        Event savedEvent = this.eventRepository.save(event);
        return this.convertToDTO(savedEvent);
    }

    /**
     * Used to create a timeslot associated to an event id
     * @param eventId
     * @param timeSlotDTO
     * @return
     */
    @Transactional
    public TimeSlotDTO saveTimeSlot(final Long eventId, final TimeSlotDTO timeSlotDTO) {
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventException(String.format("Event with id : %s not found", eventId)));
        TimeSlot timeSlot = this.timeSlotService.save(event, timeSlotDTO);
        event.getTimeSlots().add(timeSlot);
        return this.timeSlotService.convertToDTO(timeSlot);
    }

    /**
     * gent an event by id
     * @param id
     * @return
     */
    public EventDTO getEvent(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventException(String.format("Event with id : %s  not found", id)));
        return convertToDTO(event);
    }

    /**
     * get all events
     * @return
     */
    public Page<EventDTO> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = eventRepository.findAll(pageable);
        return eventsPage.map(event -> convertToDTO(event));
    }

    /**
     * deleten an event entity by id
     * @param eventId
     */
    public void delete(final Long eventId) {
        this.eventRepository.deleteById(eventId);
    }

    /**
     * build an event entity with their timeslots
     * @param eventDTO
     * @return
     */
    private Event buildEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        List<TimeSlot> timeSlots = this.timeSlotService.toTimeSlots(eventDTO.getTimeSlots());
        if (timeSlots.isEmpty()) {
            throw new EventException("Cannot create an event without a timeslot");
        }
        timeSlots.forEach(ts -> ts.setEvent(event));
        event.setTimeSlots(timeSlots);
        return event;
    }

    /**
     * convert an event entity to DTO
     * @param event
     * @return
     */
    private EventDTO convertToDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        if (event.getTimeSlots() != null) {
            eventDTO.setTimeSlots(event.getTimeSlots().stream().map(timeSlotService::convertToDTO).collect(Collectors.toList()));
        }
        return eventDTO;
    }
}
