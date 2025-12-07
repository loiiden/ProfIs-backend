package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.ReferentDto;
import com.example.profisbackend.model.Referent;

/**
 * Utility mapper that converts between ReferentDto and Referent entity.
 *
 * Rules:
 * - If the incoming DTO is null, the mapper returns null.
 * - The mapper copies fields present on the Person superclass (firstName, lastName, etc.)
 *   by calling the generated setters on the Referent entity.
 * - The mapper does not manage relationships (e.g. marks) — those should be handled
 *   by higher-level services if needed.
 */
public class ReferentMapper {
    public static Referent toEntity(ReferentDto dto){
        if(dto == null) return null;
        Referent r = new Referent();
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
     * Convert a persisted Referent into a DTO suitable for transport over HTTP.
     * The mapper reads fields inherited from Person and Evaluator.
     */
    public static ReferentDto toDto(Referent referent){
        if(referent == null) return null;
        return new ReferentDto(
                referent.getId(),
                referent.getFirstName(),
                referent.getLastName(),
                referent.getEmail(),
                referent.getPhoneNumber(),
                referent.getAcademicLevel(),
                referent.getRole()
        );
    }
}
