package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public class TimeSlotDTO {

    public TimeSlotDTO() {
    }

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private List<Long> graduateIds;

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

    public List<Long> getGraduateIds() {
        return graduateIds;
    }

    public void setGraduateIds(List<Long> graduateIds) {
        this.graduateIds = graduateIds;
    }

    @Override
    public String toString() {
        return "TimeSlotDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
