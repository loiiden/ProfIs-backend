package com.example.profisbackend.enums;

import java.util.Objects;

public enum AcademicLevel {

    NONE("Kein Abschluss"),
    BACHELOR("Bachelor"),
    MASTER("Master"),
    DR("Dr."),
    PROF("Prof."),
    PROF_DOCTOR("Prof. Dr."),
    DIPLOMA("Diplom");
    private String label;

    AcademicLevel(String label){
        this.label=label;
    }

    public static AcademicLevel valueOfLabel(String label){
        for (AcademicLevel e: values()){
            if (Objects.equals(e.label,label))
                return e;
        }
        return NONE;
    }
}
