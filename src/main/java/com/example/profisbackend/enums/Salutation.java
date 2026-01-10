package com.example.profisbackend.enums;

public enum Salutation {

    HERR("Herr"),
    FRAU("Frau"),
    DIVERS("Divers");

    private String label;
    public  String getLabel(){
        return this.label;
    }

    Salutation(String label) {
        this.label = label;
    }

    public static Salutation valueOfLabel(String label) {
        for (Salutation e : values()) {
            if (java.util.Objects.equals(e.label, label)) {
                return e;
            }
        }
        return Salutation.DIVERS;
    }
}
