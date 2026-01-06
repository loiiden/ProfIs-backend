package com.example.profisbackend.dto.studyprogram;

import com.example.profisbackend.enums.DegreeType;

public record StudyProgramResponseDTO(Long id, DegreeType degreeType, String title, double sws) {
}  
