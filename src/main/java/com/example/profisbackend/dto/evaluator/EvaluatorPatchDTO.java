package com.example.profisbackend.dto.evaluator;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;

/**
 * DTO used for PATCH updates to Evaluator resources.
 * All fields are nullable/optional to allow partial updates.
 */
public record EvaluatorPatchDTO(
        String firstName,
        String lastName,
        String address,
        String email,
        String phoneNumber,
        AcademicLevel academicLevel,
        EvaluatorRole role
) {
}

