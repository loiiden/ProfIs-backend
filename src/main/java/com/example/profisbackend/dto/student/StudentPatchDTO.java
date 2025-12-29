package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;

public record StudentPatchDTO(
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        Long studentNumber,
        AcademicLevel academicLevel
        ){}
