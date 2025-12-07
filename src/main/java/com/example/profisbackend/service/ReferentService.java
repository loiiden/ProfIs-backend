package com.example.profisbackend.service;

import org.springframework.stereotype.Service;

import com.example.profisbackend.dto.ReferentDto;
import com.example.profisbackend.mapper.ReferentMapper;
import com.example.profisbackend.model.Referent;
import com.example.profisbackend.repository.ReferentRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service layer for Referent operations.
 *
 * Responsibilities:
 * - Translate ReferentDto to entity using ReferentMapper.
 * - Persist Referent entities using ReferentRepository.
 * - Provide simple retrieval methods used by the controller.
 *
 * Note: This service currently returns the JPA entity for simplicity. In a stricter design
 * you could return DTOs or domain objects to keep a clean separation between layers.
 */
@RequiredArgsConstructor
@Service
public class ReferentService {
    private final ReferentRepository referentRepository;

    /**
     * Create and persist a Referent from a DTO.
     * @param referentDto DTO with data for the new referent (id may be null).
     * @return persisted Referent entity with generated id.
     */
    public Referent createReferent(ReferentDto referentDto) {
        Referent toSave = ReferentMapper.toEntity(referentDto);
        return referentRepository.save(toSave);
    }

    /**
     * Find a Referent by id.
     * @param id primary key
     * @return Referent entity or null if not present
     */
    public Referent findById(Long id){
        return referentRepository.findById(id).orElse(null);
    }
}
