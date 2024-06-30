package com.utn.graduates.service;

import com.utn.graduates.dto.AttendanceDTO;
import com.utn.graduates.exception.AttendanceException;
import com.utn.graduates.model.Attendance;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.model.TimeSlot;
import com.utn.graduates.repository.AttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final GraduateService graduateService;
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(GraduateService graduateService, AttendanceRepository attendanceRepository) {
        this.graduateService = graduateService;
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * used to create all the attendances for an existing event
     * @param attendanceDTOs
     * @param timeSlot
     * @return
     */
    public List<Attendance> saveAll(List<AttendanceDTO> attendanceDTOs, TimeSlot timeSlot) {
        List<Attendance> attendances = attendanceDTOs.stream().map(dto -> {
            Graduate graduate = this.graduateService.getGraduateById(dto.getGraduateId());
            return this.convertToEntity(dto, timeSlot, graduate);
        }).collect(Collectors.toList());
        return this.attendanceRepository.saveAll(attendances);
    }

    /**
     * Only used to mark a present in true or false
     * @param attendanceDTO
     */
    @Transactional
    public AttendanceDTO updatePresent(AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceRepository.findById(attendanceDTO.getId()).orElseThrow(() ->
                new AttendanceException(String.format("Attendance with id: %s not found", attendanceDTO.getId())));
        attendance.setPresent(attendanceDTO.isPresent());
        return this.convertToDTO(attendance);
    }

    /**
     * used to create an Attendance linked with an graduate and a timeslot
     * @param attendanceDTO
     * @param timeSlot
     * @param graduate
     * @return
     */
    public Attendance convertToEntity(AttendanceDTO attendanceDTO, TimeSlot timeSlot, Graduate graduate) {
        Attendance attendance = new Attendance();
        attendance.setId(attendanceDTO.getId());
        attendance.setGraduate(graduate);
        attendance.setTimeSlot(timeSlot);
        attendance.setPresent(attendanceDTO.isPresent());
        return attendance;
    }

    public AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(attendance.getId());
        attendanceDTO.setGraduateId(attendance.getGraduate().getId());
        attendanceDTO.setTimeSlotId(attendance.getTimeSlot().getId());
        attendanceDTO.setFullname(attendance.getGraduate().getFullname());
        attendanceDTO.setDni(attendance.getGraduate().getDni());
        attendanceDTO.setSpecialty(attendance.getGraduate().getSpecialty());
        attendanceDTO.setPresent(attendance.isPresent());
        return attendanceDTO;
    }

    public Set<Long> getAttendancesGraduateIdsByTimeSlotId(final Long timeSlotId) {
        List<Attendance> attendances = attendanceRepository.findAllByTimeSlotId(timeSlotId);
        return attendances.stream().map(attendance -> attendance.getGraduate().getId()).collect(Collectors.toSet());
    }
}
