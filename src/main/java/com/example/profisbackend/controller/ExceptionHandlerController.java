package com.example.profisbackend.controller;

import com.example.profisbackend.exceptions.ErrorResponse;
import com.example.profisbackend.exceptions.StudentNotFoundException;
import com.example.profisbackend.exceptions.StudyProgramNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionHandlerController {
    private ErrorResponse buildErrorResponse(HttpServletRequest request, HttpStatus status, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
    @ExceptionHandler(StudyProgramNotFound.class)
    public ResponseEntity<ErrorResponse> handleStudyProgramNotFound(
            StudyProgramNotFound ex,
            HttpServletRequest request ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = buildErrorResponse(
                request,
                status,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(
            StudentNotFoundException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = buildErrorResponse(
                request,
                status,
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
