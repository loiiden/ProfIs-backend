package com.example.profisbackend.exceptions;

public class StudyProgramNotFound extends RuntimeException {
    public StudyProgramNotFound(Long id){
        super("Study Program Not Found. Id: " + id);
    }
}
