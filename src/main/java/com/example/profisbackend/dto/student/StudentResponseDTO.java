package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.Salutation;

import java.util.List;

public record StudentResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        Long studentNumber,
        Salutation salutation,
        AcademicLevel academicLevel,
        List<Long> scientificWorksIds,
        Long studyProgramId
) {
}
