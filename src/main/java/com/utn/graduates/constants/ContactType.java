package com.utn.graduates.constants;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "contact_type")
@Entity
public class ContactType {
    @Id
    private String value;

    public ContactType() {
    }

    public ContactType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
