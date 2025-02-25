package com.utn.graduates.model;

import com.utn.graduates.constants.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;


@Table(name = "graduate", indexes = {
        @Index(name = "idx_fullname", columnList = "fullname"),
        @Index(name = "idx_dni", columnList = "dni", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_phone", columnList = "phone", unique = true)
})
@Entity
public class Graduate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    @Column(unique = true)
    private String dni;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "contact_type_name", referencedColumnName = "name")
    private ContactType contactType;

    @ManyToOne
    @JoinColumn(name = "specialty_name", referencedColumnName = "name")
    private Specialty specialty;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
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

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(final Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Graduate graduate = (Graduate) o;
        return Objects.equals(dni, graduate.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
