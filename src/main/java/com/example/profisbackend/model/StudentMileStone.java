package com.example.profisbackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentMileStone {
    private String scientificWorkTitle;
    private String studyProgramTitle;

    public StudentMileStone(String scientificWorkTitle, String studyProgramTitle) {
        this.scientificWorkTitle = scientificWorkTitle;
        this.studyProgramTitle = studyProgramTitle;
    }
}
