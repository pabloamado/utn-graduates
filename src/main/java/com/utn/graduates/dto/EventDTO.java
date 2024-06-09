package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {

    public EventDTO() {
    }

    private Long id;

    private String name;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private List<TimeSlotDTO> timeSlots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

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

    public List<TimeSlotDTO> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotDTO> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeSlots=" + timeSlots +
                '}';
    }
}
