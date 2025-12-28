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

public record ScientificWorkResponseDTO(
        Long id,
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
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Long studentId,
        Long studyProgramId,
        Long mainEvaluatorId,
        Integer mainEvaluatorWorkMark,
        Integer mainEvaluatorColloquiumMark,
        Long secondEvaluatorId,
        Integer secondEvaluatorWorkMark,
        Integer secondEvaluatorColloquiumMark
) {
}
