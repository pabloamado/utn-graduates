package com.utn.graduates.dto;

public class ContactTypeDTO {
    private String name;

    public ContactTypeDTO() {
    }

    public ContactTypeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
