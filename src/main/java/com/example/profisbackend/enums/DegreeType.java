package com.example.profisbackend.enums;

public enum DegreeType {
    NONE("NONE"),
    B_A("B.A."),
    B_ENG("B.Eng."),
    B_SC("B.Sc."),
    B_LAWS("B.Laws"), 
    M_A("M.A."),
    M_SC("M.Sc."),
    M_ENG("M.Eng."),
    LL_M("LL.M."),
    MBA("MBA");

    private String label;
    public String getLabel(){
        return this.label;
    }

    DegreeType(String label) {
        this.label = label;
    }

    public static DegreeType valueOfLabel(String label) {
        for (DegreeType e : values()) {
            if (java.util.Objects.equals(e.label, label)) {
                return e;
            }
        }
        return DegreeType.NONE;
    }
}
