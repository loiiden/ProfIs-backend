package com.example.profisbackend.dto.scientificWork;

import com.example.profisbackend.dto.event.EventShortResponseDTO;

public record ScientificWorkShortDTO(Long id, EventShortResponseDTO status, String title, String StudyProgramTitle) {
}
