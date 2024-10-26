package com.utn.graduates.dto;

import jakarta.validation.constraints.NotNull;

public class ContactTypeDTO {
    @NotNull
    private String value;

    public ContactTypeDTO() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
