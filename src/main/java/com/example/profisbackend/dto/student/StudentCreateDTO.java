package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.Salutation;
//TODO: Discuss if photo is needed. Not implemented yet.
public record StudentCreateDTO(
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        Long studentNumber,
        Salutation salutation,
        AcademicLevel academicLevel,
        Long studyProgramId
        ) {

}

