package com.example.profisbackend.dto.scientificWork;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScientificWorkResponseDTO(
        Long id,
        LocalDateTime colloquium,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Long studentId,
        Long studyProgramId
) {
}
