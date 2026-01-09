package com.example.profisbackend.dto.scientificWork;

import java.time.LocalDate;

public record EventSummaryDTO(
        String name,
        LocalDate date
) {
}

