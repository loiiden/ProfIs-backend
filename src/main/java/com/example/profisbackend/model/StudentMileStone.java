package com.example.profisbackend.model;

public class StudentMileStone {
    private String scientificWorkTitle;
    private String studyProgramTitle;

    public StudentMileStone(String scientificWorkTitle, String studyProgramTitle){
        this.scientificWorkTitle=scientificWorkTitle;
        this.studyProgramTitle=studyProgramTitle;
    }

    //TODO make usage of lombok later
    public String getScientificWorkTitle(){
        return this.scientificWorkTitle;
    }
      public String getStudyProgramTitle(){
        return this.studyProgramTitle;
    }

}
