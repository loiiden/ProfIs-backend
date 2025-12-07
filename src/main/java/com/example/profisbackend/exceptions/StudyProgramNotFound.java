package com.example.exception;

public class StudyProgramNotFound extends RuntimeException {
    public StudyProgramNotFound(String message){
        super(message);
    }
}
