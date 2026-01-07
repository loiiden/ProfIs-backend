package com.example.profisbackend.dto;

import java.util.List;

// Summary DTO returned to frontend for a single student including their scientific works,
// timeline events and aggregated scores.
public record StudentSummaryDTO(
        Long id,
        String firstName,
        String lastName,
        Long studentNumber,
        List<ScientificWorkSummaryDTO> scientificWorks
) {
}
