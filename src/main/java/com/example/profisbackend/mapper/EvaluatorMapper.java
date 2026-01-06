package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.evaluator.EvaluatorResponseDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.dto.evaluator.EvaluatorPatchDTO;
import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;

/**
 * Utility mapper that converts between EvaluatorDto and Evaluator entity.
 *
 * Rules:
 * - If the incoming DTO is null, the mapper returns null.
 * - The mapper copies fields present on the Person superclass (firstName,
 * lastName, etc.)
 * by calling the generated setters on the Evaluator entity.
 * - The mapper does not manage relationships (e.g. marks) — those should be
 * handled
 * by higher-level services if needed.
 */
public class EvaluatorMapper {
    /**
     * Create an entity from a create-only DTO. This intentionally does not copy an
     * id.
     */
    public static Evaluator toEntity(EvaluatorCreateDTO dto) {
        if (dto == null)
            return null;
        Evaluator r = new Evaluator();
        r.setFirstName(dto.firstName());
        r.setLastName(dto.lastName());
        r.setAddress(dto.address());
        r.setEmail(dto.email());
        r.setPhoneNumber(dto.phoneNumber());
        r.setAcademicLevel(dto.academicLevel());
        r.setRole(dto.role());
        r.setSalutation(dto.salutation());
        return r;
    }

    /**
     * Convert a persisted Evaluator into a DTO suitable for transport over HTTP.
     * The mapper reads fields inherited from Person and Evaluator.
     */
    public static EvaluatorResponseDTO toDto(Evaluator Evaluator) {
        if (Evaluator == null)
            return null;
        return new EvaluatorResponseDTO(
                Evaluator.getId(),
                Evaluator.getFirstName(),
                Evaluator.getLastName(),
                Evaluator.getAddress(),
                Evaluator.getEmail(),
                Evaluator.getPhoneNumber(),
                Evaluator.getAcademicLevel(),
                Evaluator.getRole(),
                Evaluator.getScientificWorksAsMainEvaluator().stream().map(ScientificWork::getId).toList(),
                Evaluator.getScientificWorksAsSecondEvaluator().stream().map(ScientificWork::getId).toList(),
                Evaluator.getSalutation()
        );
    }

    /**
     * Apply non-null fields from the DTO to an existing entity. This enables
     * partial updates (PATCH). The dto.id() is ignored to avoid changing the
     * primary key.
     */
    public static void updateEntityFromDto(Evaluator entity, EvaluatorPatchDTO dto) {
        if (entity == null || dto == null)
            return;
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setAddress(dto.address());
        entity.setEmail(dto.email());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setAcademicLevel(dto.academicLevel());
        entity.setRole(dto.role());
        entity.setSalutation(dto.salutation());
    }
}
