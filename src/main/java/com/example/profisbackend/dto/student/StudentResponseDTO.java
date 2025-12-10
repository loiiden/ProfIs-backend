package com.example.profisbackend.dto.student;

import com.example.profisbackend.enums.AcademicLevel;

import java.util.List;

//do not delete this.
//It could be needed in order to control what is exposed to frontend!
public record StudentResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        Long studentNumber,
        AcademicLevel academicLevel,
        List<Long> scientificWorksIds
) {
}
