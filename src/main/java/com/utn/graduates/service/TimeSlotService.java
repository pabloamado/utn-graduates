package com.utn.graduates.service;

import com.utn.graduates.dto.TimeSlotDTO;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.model.TimeSlot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {
    private final GraduateService graduateService;

    public TimeSlotService(GraduateService graduateService){
        this.graduateService = graduateService;
    }

    public List<TimeSlot> toTimeSlots(final List<TimeSlotDTO> timeSlotsDTOs) {
        return timeSlotsDTOs.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    private TimeSlot convertToEntity(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeSlotDTO.getStartTime());
        timeSlot.setEndTime(timeSlotDTO.getEndTime());

        List<Graduate> graduates = graduateService.findAllById(timeSlotDTO.getGraduateIds());
        if (graduates.size() != timeSlotDTO.getGraduateIds().size()) {
            throw new IllegalArgumentException("Some graduates were not found");
        }
        timeSlot.setGraduates(graduates);
        return timeSlot;
    }

    public TimeSlotDTO convertToDTO(TimeSlot timeSlot) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setStartTime(timeSlot.getStartTime());
        timeSlotDTO.setEndTime(timeSlot.getEndTime());
        timeSlotDTO.setGraduateIds(timeSlot.getGraduates().stream().map(Graduate::getId).collect(Collectors.toList()));
        return timeSlotDTO;
    }

}
