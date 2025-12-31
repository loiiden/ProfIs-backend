package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.Salutation;

public record StudentPatchDTO(
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        Long studentNumber,
        AcademicLevel academicLevel,
        Salutation salutation
        ){}
