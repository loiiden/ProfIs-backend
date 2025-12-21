package com.example.profisbackend.dto.evaluator;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;

import java.util.List;

/**
 * Data Transfer Object for Evaluator resources exposed by the REST API.
 *
 * Fields:
 * - id            : database id (null for create requests)
 * - firstName     : given name
 * - lastName      : family name
 * - email         : contact email
 * - phoneNumber   : contact phone
 * - academicLevel : academic qualification (enum)
 * - role          : evaluator role (enum)
 *
 * This DTO intentionally avoids including relational collections (e.g. marks)
 * to keep the payload small for the initial API.
 */
public record EvaluatorResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AcademicLevel academicLevel,
        EvaluatorRole role,
        List<Long> scientificWorksIdsAsMainEvaluator,
        List<Long> scientificWorksIdsAsSecondEvaluator
) {
}
