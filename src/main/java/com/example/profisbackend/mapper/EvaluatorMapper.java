package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.EvaluatorDto;
import com.example.profisbackend.model.Evaluator;

/**
 * Utility mapper that converts between EvaluatorDto and Evaluator entity.
 *
 * Rules:
 * - If the incoming DTO is null, the mapper returns null.
 * - The mapper copies fields present on the Person superclass (firstName, lastName, etc.)
 *   by calling the generated setters on the Evaluator entity.
 * - The mapper does not manage relationships (e.g. marks) — those should be handled
 *   by higher-level services if needed.
 */
public class EvaluatorMapper {
    public static Evaluator toEntity(EvaluatorDto dto){
        if(dto == null) return null;
        Evaluator r = new Evaluator();
        r.setId(dto.id());
        r.setFirstName(dto.firstName());
        r.setLastName(dto.lastName());
        r.setEmail(dto.email());
        r.setPhoneNumber(dto.phoneNumber());
        r.setAcademicLevel(dto.academicLevel());
        r.setRole(dto.role());
        return r;
    }

    /**
     * Convert a persisted Evaluator into a DTO suitable for transport over HTTP.
     * The mapper reads fields inherited from Person and Evaluator.
     */
    public static EvaluatorDto toDto(Evaluator Evaluator){
        if(Evaluator == null) return null;
        return new EvaluatorDto(
                Evaluator.getId(),
                Evaluator.getFirstName(),
                Evaluator.getLastName(),
                Evaluator.getEmail(),
                Evaluator.getPhoneNumber(),
                Evaluator.getAcademicLevel(),
                Evaluator.getRole()
        );
    }

    /**
     * Apply non-null fields from the DTO to an existing entity. This enables
     * partial updates (PATCH). The dto.id() is ignored to avoid changing the primary key.
     */
    public static void updateEntityFromDto(Evaluator entity, EvaluatorDto dto) {
        if(entity == null || dto == null) return;
        if(dto.firstName() != null) entity.setFirstName(dto.firstName());
        if(dto.lastName() != null) entity.setLastName(dto.lastName());
        if(dto.email() != null) entity.setEmail(dto.email());
        if(dto.phoneNumber() != null) entity.setPhoneNumber(dto.phoneNumber());
        if(dto.academicLevel() != null) entity.setAcademicLevel(dto.academicLevel());
        if(dto.role() != null) entity.setRole(dto.role());
        // id is intentionally not updated here
    }
}
