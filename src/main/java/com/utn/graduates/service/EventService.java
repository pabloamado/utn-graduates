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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
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

    /**
     * create an event with timeslots
     * @param eventDTO
     * @return
     */
    @Transactional
    public EventDTO save(EventDTO eventDTO) {
        Event event = this.buildEvent(eventDTO);
        this.validEvent(event);
        List<Event> events = this.eventRepository.findAll();
        Event founded = events.stream().filter(e -> e.getName().equalsIgnoreCase(event.getName())).findFirst().orElse(null);
        if (founded != null) {
            throw new EventException("Event with name " + event.getName() + " already exists");
        }
        Event savedEvent = this.eventRepository.save(event);
        return this.convertToDTO(savedEvent);
    }

    private static void validEvent(Event event) {
        if (event.getEndTime() == null || event.getStartTime() == null) {
            throw new EventException("the event need to have endTime and startTime.");
        }
        if (!StringUtils.hasText(event.getName())) {
            throw new EventException("the event need a name.");
        }
        if (event.getStartTime().isAfter(event.getEndTime())) {
            throw new EventException(String.format("event start time can´t be after end time startTime: %s, endTime: %s", event.getStartTime(),
                    event.getEndTime()));
        }
        if (event.getEndTime().isBefore(event.getStartTime())) {
            throw new EventException(String.format("event start time can't be before start time. startTime: %s, TimeSlot startTime: %s",
                    event.getStartTime(), event.getStartTime()));
        }
        LocalDate now = LocalDate.now();
        if (event.getDate() == null || event.getDate().isBefore(now)) {
            throw new EventException(String.format("event date can't be empty or be before today. date: %s today: %s", event.getDate(), now));
        }
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
