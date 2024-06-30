package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttendanceDTO implements Serializable {
    private Long id;
    private Long graduateId;
    private Long timeSlotId;
    private String fullname;
    private String dni;
    private String specialty;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(final String dni) {
        this.dni = dni;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(final String specialty) {
        this.specialty = specialty;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(final boolean present) {
        this.present = present;
    }
}
