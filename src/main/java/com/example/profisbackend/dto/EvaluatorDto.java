package com.example.profisbackend.dto;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.EvaluatorRole;

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
public record EvaluatorDto(
        //TODO: need to create EvaluatorCreateDTO without ID as use it for creation!
        // it currently breaks if I try to post a new evaluator with id provided.
        // Critical!
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AcademicLevel academicLevel,
        EvaluatorRole role
) {
}
