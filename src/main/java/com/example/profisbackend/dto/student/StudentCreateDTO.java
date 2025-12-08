package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;
//TODO: Discuss if photo is needed. Not implemented yet.
public record StudentCreateDTO(
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        AcademicLevel academicLevel
        ) {

}

