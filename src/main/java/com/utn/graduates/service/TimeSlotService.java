package com.utn.graduates.service;

import com.utn.graduates.dto.AttendanceDTO;
import com.utn.graduates.dto.TimeSlotDTO;
import com.utn.graduates.exception.TimeSlotException;
import com.utn.graduates.model.Attendance;
import com.utn.graduates.model.Event;
import com.utn.graduates.model.TimeSlot;
import com.utn.graduates.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final AttendanceService attendanceService;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, AttendanceService attendanceService) {
        this.timeSlotRepository = timeSlotRepository;
        this.attendanceService = attendanceService;
    }

    /**
     * Used to create a timeslot associated to an event id
     * @param event
     * @param timeSlotDTO
     * @return
     */
    @Transactional
    public TimeSlot save(Event event, TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = this.convertToEntity(timeSlotDTO);
        this.validTimeSlot(this.convertToDTO(timeSlot), event);
        List<TimeSlot> timeSlots = timeSlotRepository.findByEventId(event.getId());
        boolean existed = timeSlots.stream().anyMatch(t -> t.getName().equalsIgnoreCase(timeSlot.getName()));
        if (existed) {
            throw new TimeSlotException("Seccion con nombre " + timeSlot.getName() + " ya existe.");
        }
        timeSlot.setEvent(event);
        return this.timeSlotRepository.save(timeSlot);
    }

    /**
     * update timeslot data
     * @param timeSlotDTO
     */
    @Transactional
    public TimeSlotDTO updateTimeSlot(final TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotDTO.getId())
                .orElseThrow(() -> new TimeSlotException(String.format("La Seccion con id : %s no fue encontrada.", timeSlotDTO.getId())));
        this.validTimeSlot(this.convertToDTO(timeSlot), timeSlot.getEvent());
        timeSlot.setStartTime(timeSlotDTO.getStartTime());
        timeSlot.setEndTime(timeSlotDTO.getEndTime());
        List<AttendanceDTO> attendanceDTOs = timeSlotDTO.getAttendances();
        Set<Long> graduateIds = attendanceService.getAttendancesGraduateIdsByTimeSlotId(timeSlotDTO.getId());
        List<AttendanceDTO> filteredAttendancesDTOs = attendanceDTOs.stream()
                .filter(attendanceDTO -> !graduateIds.contains(attendanceDTO.getGraduateId()))
                .collect(Collectors.toList());
        if (!filteredAttendancesDTOs.isEmpty()) {
            List<Attendance> attendances = this.attendanceService.saveAll(filteredAttendancesDTOs, timeSlot);
            timeSlot.getAttendances().addAll(attendances);
        }

        return convertToDTO(timeSlotRepository.save(timeSlot));
    }

    public void validTimeSlot(final TimeSlotDTO timeSlot, Event event) {
        if (!StringUtils.hasText(timeSlot.getName())) {
            throw new TimeSlotException("La seccion necesita un nombre.");
        }
        if (timeSlot.getEndTime() == null || timeSlot.getStartTime() == null) {
            throw new TimeSlotException("La seccion debe tener hora de inicio y finalizacion.");
        }
        if (timeSlot.getStartTime().isAfter(timeSlot.getEndTime())) {
            throw new TimeSlotException(String.format("La hora de inicio de la seccion no puede ser despues de la hora de finalizacion. Hora de inicio: %s, Hora de finalizacion: %s.", timeSlot.getStartTime(), timeSlot.getEndTime()));
        }
        if (timeSlot.getStartTime().isBefore(event.getStartTime())) {
            throw new TimeSlotException(String.format("La hora de inicio de la seccion no puede ser antes de la hora de inicio del evento. Hora de inicio de la seccion: %s, Hora de inicio del evento: %s.",
                    timeSlot.getStartTime(), event.getStartTime()));
        }
        if (timeSlot.getStartTime().isAfter(event.getEndTime())) {
            throw new TimeSlotException(String.format("La hora de inicio de la seccion no puede ser despues de la hora de finalizacion del evento. Hora de inicio de seccion: %s, Hora de finalizacion del evento: %s",
                    timeSlot.getStartTime(), event.getEndTime()));
        }
        if (timeSlot.getEndTime().isAfter(event.getEndTime())) {
            throw new TimeSlotException(String.format("La hora de finalizacion de la seccion no puede ser despues de la hora de finalizacion del evento. Hora de finalizacion de la seccion: %s, Hora de finalizacion del evento: %s.",
                    timeSlot.getEndTime(), event.getEndTime()));
        }
    }

    public void delete(final Long timeSlotId) {
        this.timeSlotRepository.deleteById(timeSlotId);
    }

    public TimeSlot getTimeSlotById(final Long id) {
        return this.timeSlotRepository.findById(id)
                .orElseThrow(() -> new TimeSlotException(String.format("TimeSlot with id: %s not found")));
    }

    public List<TimeSlot> toTimeSlots(final List<TimeSlotDTO> timeSlotsDTOs) {
        return timeSlotsDTOs.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    private TimeSlot convertToEntity(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setName(timeSlotDTO.getName());
        timeSlot.setStartTime(timeSlotDTO.getStartTime());
        timeSlot.setEndTime(timeSlotDTO.getEndTime());
        return timeSlot;
    }

    /**
     * convert a timeslot into a DTO
     * @param timeSlot
     * @return
     */
    public TimeSlotDTO convertToDTO(TimeSlot timeSlot) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setId(timeSlot.getId());
        timeSlotDTO.setName(timeSlot.getName());
        timeSlotDTO.setStartTime(timeSlot.getStartTime());
        timeSlotDTO.setEndTime(timeSlot.getEndTime());
        if (!CollectionUtils.isEmpty(timeSlot.getAttendances())) {
            List<Attendance> attendances = timeSlot.getAttendances();
            timeSlotDTO.setAttendances(attendances
                    .stream()
                    .map(attendanceService::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return timeSlotDTO;
    }

}
