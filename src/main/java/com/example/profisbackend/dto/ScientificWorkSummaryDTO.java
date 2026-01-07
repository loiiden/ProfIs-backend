package com.example.profisbackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ScientificWorkSummaryDTO(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime colloquium,
        List<WorkEventDTO> events,
        Double avgScientificWorkScore,
        Double avgColloquiumScore
) {
}

