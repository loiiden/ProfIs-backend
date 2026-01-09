package com.example.profisbackend.dto.scientificWork;

import java.util.List;

public record ScientificWorkSummaryDTO(
        Long id,
        String firstName,
        String lastName,
        String title,
        String studyProgramTitle,
        String referentName,
        String coReferentName,
        Integer referentGrade,
        Integer coReferentGrade,
        List<EventSummaryDTO> events
) {
}
