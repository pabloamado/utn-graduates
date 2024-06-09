package com.utn.graduates.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utn.graduates.constants.ContactType;
import com.utn.graduates.constants.Genre;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraduateDTO implements Serializable {
    private Long id;
    @NotNull
    private String fullname;
    @NotNull
    private String dni;
    private boolean present;
    @NotNull
    private Genre genre;
    @NotNull
    private ContactType contactType;

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

    public boolean isPresent() {
        return present;
    }

    public void setPresent(final boolean present) {
        this.present = present;
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

    @Override
    public String toString() {
        return "GraduateDTO{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", present=" + present +
                ", genre=" + genre +
                ", contactType=" + contactType +
                '}';
    }

}
