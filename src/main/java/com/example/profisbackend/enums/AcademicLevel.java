package com.example.profisbackend.enums;

import java.util.Objects;

public enum AcademicLevel {

    NONE("Kein Abschluss", 0),
    BACHELOR("Bachelor", 1),
    MASTER("Master", 2),
    DR("Dr.", 3),
    PROF("Prof.", 4),
    PROF_DOCTOR("Prof. Dr.", 5),
    DIPLOMA("Diplom", 4);

    private String label;
    private Integer level;
    public  String  getLabel(){
        return this.label;
    }


    AcademicLevel(String label, Integer level) {
        this.label = label;
        this.level = level;
    }
    public boolean isHigherThan(AcademicLevel otherLevel) {
        return this.level > otherLevel.level;
    }
    public boolean isLowerThan(AcademicLevel otherLevel) {
        return this.level < otherLevel.level;
    }
    public boolean isEqualLevel(AcademicLevel otherLevel) {
        return this.level.equals(otherLevel.level);
    }


    public static AcademicLevel valueOfLabel(String label) {
        for (AcademicLevel e : values()) {
            if (Objects.equals(e.label, label))
                return e;
        }
        return NONE;
    }
}
