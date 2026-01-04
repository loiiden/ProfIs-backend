package com.example.profisbackend.dto.scientificWork;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//marks are not possible to specify while creating a new work
public record ScientificWorkCreateDTO(
    LocalDateTime colloquium,
    String colloquiumLocation,
    Duration colloquiumDuration,
    @JsonFormat(pattern = "HH:mm")
    LocalTime presentationStart,
    @JsonFormat(pattern = "HH:mm")
    LocalTime presentationEnd,
    @JsonFormat(pattern = "HH:mm")
    LocalTime discussionStart,
    @JsonFormat(pattern = "HH:mm")
    LocalTime discussionEnd,
    @NotNull(message = "Title can not be null")
    @NotBlank(message = "Title can not be blank")
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Long studentId,
    Long studyProgramId,
    Long mainEvaluatorId,

    @Min(0)
    @Max(100)
    Integer mainEvaluatorWorkMark,

    @Min(0)
    @Max(100)
    Integer mainEvaluatorColloquiumMark,

    Long secondEvaluatorId,
    @Min(0)
    @Max(100)
    Integer secondEvaluatorWorkMark,

    @Min(0)
    @Max(100)
    Integer secondEvaluatorColloquiumMark,

    String comment
) {
}
