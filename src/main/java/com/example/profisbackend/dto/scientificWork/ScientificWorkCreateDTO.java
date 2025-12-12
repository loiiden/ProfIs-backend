package com.example.profisbackend.dto.scientificWork;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

//marks are not possible to specify while creating a new work
public record ScientificWorkCreateDTO(
    LocalDateTime colloquium,
    @NotNull(message = "Title can not be null")
    @NotBlank(message = "Title can not be blank")
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Long studentId,
    Long studyProgramId
) {
}
