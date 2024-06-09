package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utn.graduates.model.Attendance;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlotDTO {

    public TimeSlotDTO() {
    }

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private List<AttendanceDTO> attendancesDTO = new ArrayList<>();

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<AttendanceDTO> getAttendancesDTO() {
        return attendancesDTO;
    }

    public void setAttendancesDTO(final List<AttendanceDTO> attendancesDTO) {
        this.attendancesDTO = attendancesDTO;
    }

    @Override
    public String toString() {
        return "TimeSlotDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
