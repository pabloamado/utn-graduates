package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utn.graduates.constants.ContactType;
import com.utn.graduates.constants.Genre;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraduateDTO implements Serializable {
    private Long id;

    private String fullname;

    private String dni;

    private Genre genre;

    private ContactType contactType;

    private String specialty;

    public GraduateDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(final ContactType contactType) {
        this.contactType = contactType;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(final String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "GraduateDTO{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", genre=" + genre +
                ", contactType=" + contactType +
                ", specialty=" + specialty +
                '}';
    }

}
