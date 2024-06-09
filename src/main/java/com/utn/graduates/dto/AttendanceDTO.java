package com.utn.graduates.dto;

public class AttendanceDTO {
    private Long id;
    private Long graduateId;
    private Long timeSlotId;
    private boolean present;

    public AttendanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGraduateId() {
        return graduateId;
    }

    public void setGraduateId(final Long graduateId) {
        this.graduateId = graduateId;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(final Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(final boolean present) {
        this.present = present;
    }
}
