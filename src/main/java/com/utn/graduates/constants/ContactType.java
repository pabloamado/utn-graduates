package com.utn.graduates.constants;

public enum ContactType {
    OTHER("otro"),
    EXTENSION("extension"),
    RESEARCH("investigador"),
    ADVISER("consejero"),
    REFERRED("referido"),
    CLUB("club"),
    NON_TEACHING("no docente");

    private String translation;

    ContactType(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public static ContactType valueFromTranslation(String translation) {
        for (ContactType contactType : ContactType.values()) {
            if (contactType.getTranslation().equalsIgnoreCase(translation)) {
                return contactType;
            }
        }
        return null;
    }
}
