package com.example.profisbackend.dto.scientificWork;

public record ScientificWorkSwsReportDTO(
        String title,
        String studentFullName,
        Long studentStudentNumber,
        String studyProgramTitle,
        Double sws
) {
}
