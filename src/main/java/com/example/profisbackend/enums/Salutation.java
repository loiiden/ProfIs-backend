package com.example.profisbackend.enums;

public enum Salutation {
    HERR("Herr"),
    FRAU("Frau");
    
    private String salutation;
    private Salutation (String salutation){
        this.salutation=salutation;
    }

}
