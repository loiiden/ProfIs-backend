package com.example.profisbackend.dto.scientificWork;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScientificWorkPatchDTO(
        LocalDateTime colloquium,
        @NotNull(message = "Title can not be null")
        @NotBlank(message = "Title can not be blank")
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Long studentId,
        Long studyProgramId,
        Long mainEvaluatorId,
        Long secondEvaluatorId
) {
}
