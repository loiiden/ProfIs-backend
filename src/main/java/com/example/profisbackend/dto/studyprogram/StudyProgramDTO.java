package com.example.profisbackend.dto.studyprogram;

import com.example.profisbackend.enums.DegreeType;

public record StudyProgramDTO(DegreeType degreeType, String title, float sws) {
}

