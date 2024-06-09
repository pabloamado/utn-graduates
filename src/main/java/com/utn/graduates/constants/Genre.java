package com.utn.graduates.constants;

public enum Genre {
    MALE("M", "masculino"),
    FEMALE("F", "femenino");

    private String initial;
    private String translation;

    Genre(String initial, String translation) {
        this.initial = initial;
        this.translation = translation;
    }

    public String getInitial() {
        return initial;
    }

    public String getTranslation() {
        return translation;
    }

    public static Genre valueFromFields(String value){
        for(Genre genre : Genre.values()){
            if(genre.getInitial().equalsIgnoreCase(value) || genre.getTranslation().equalsIgnoreCase(value)){
                return genre;
            }
        }
        return null;
    }
}
