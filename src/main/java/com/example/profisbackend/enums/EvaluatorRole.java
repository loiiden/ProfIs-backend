package com.example.profisbackend.enums;

public enum EvaluatorRole {
    PROFESSOR("Professor"),
    EXTERNAL("Extern");
    private String label;

    EvaluatorRole(String label) {
        this.label = label;
    }

    public static EvaluatorRole valueOfLabel(String label) {
        for (EvaluatorRole e : values()) {
            if (java.util.Objects.equals(e.label, label)) {
                return e;
            }
        }
        return EXTERNAL; // may change later

    }
}