package com.example.profisbackend.dto.evaluator;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;
import com.example.profisbackend.enums.Salutation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO used when creating a new Evaluator via the API.
 * This intentionally does not contain an `id` field so callers cannot
 * set the database id on creation.
 */
public record EvaluatorCreateDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String address,
        @NotBlank String email,
        String phoneNumber,
        AcademicLevel academicLevel,
        @NotNull EvaluatorRole role,
        Salutation salutation

) {
}

