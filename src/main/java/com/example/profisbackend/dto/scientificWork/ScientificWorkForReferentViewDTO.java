package com.example.profisbackend.dto.scientificWork;

import com.example.profisbackend.dto.event.EventShortResponseDTO;

//this representation is used on the right side of referent table
public record ScientificWorkForReferentViewDTO(Long id, EventShortResponseDTO status, String studentFirstName, String studentLastName, String studyProgramTitle, String title) {
}
