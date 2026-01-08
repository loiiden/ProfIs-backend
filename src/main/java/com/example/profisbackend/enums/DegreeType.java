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

    private String abbrevation;
    public String getAbbrevation(){
        return this.abbrevation;
    }

    DegreeType(String abbrevation) {
        this.abbrevation = abbrevation;
    }

    public static DegreeType valueOfLabel(String abbrevation) {
        for (DegreeType e : values()) {
            if (java.util.Objects.equals(e.abbrevation, abbrevation)) {
                return e;
            }
        }
        return DegreeType.NONE;
    }
}
