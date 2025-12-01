package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.StudyProgramNotFound;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(StudyProgramNotFound.class)
    public ResponseEntity<String> handleException() {
        return ResponseEntity.badRequest().body("Studyprogram not found");
    }
}
