package com.utn.graduates.dto;

public class SpecialtyDTO {
    private String name;

    public SpecialtyDTO() {
    }

    public SpecialtyDTO(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
