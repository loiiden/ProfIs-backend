package com.example.profisbackend.service;

import org.springframework.stereotype.Service;

import com.example.profisbackend.dto.EvaluatorDto;
import com.example.profisbackend.mapper.EvaluatorMapper;
import com.example.profisbackend.model.Evaluator;
import com.example.profisbackend.repository.EvaluatorRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Evaluator operations.
 *
 * Responsibilities:
 * - Translate EvaluatorDto to entity using EvaluatorMapper.
 * - Persist Evaluator entities using EvaluatorRepository.
 * - Provide simple retrieval methods used by the controller.
 *
 * Note: This service currently returns the JPA entity for simplicity. In a stricter design
 * you could return DTOs or domain objects to keep a clean separation between layers.
 */
@RequiredArgsConstructor
@Service
public class EvaluatorService {
    private final EvaluatorRepository evaluatorRepository;

    /**
     * Create and persist a Evaluator from a DTO.
     * @param evaluatorDto DTO with data for the new Evaluator (id may be null).
     * @return persisted Evaluator entity with generated id.
     */
    public Evaluator createEvaluator(EvaluatorDto evaluatorDto) {
        Evaluator toSave = EvaluatorMapper.toEntity(evaluatorDto);
        return evaluatorRepository.save(toSave);
    }

    /**
     * Find a Evaluator by id.
     * @param id primary key
     * @return Evaluator entity or null if not present
     */
    public Evaluator findById(Long id){
        return evaluatorRepository.findById(id).orElse(null);
    }

    /**
     * Return all Evaluators.
     */
    public List<Evaluator> findAll(){
        return evaluatorRepository.findAll();
    }

    /**
     * Patch (partial update) an existing Evaluator. Non-null fields from the DTO
     * are applied to the existing entity and saved. If the entity does not exist,
     * returns Optional.empty().
     */
    public Optional<Evaluator> patchEvaluator(Long id, EvaluatorDto dto){
        Optional<Evaluator> maybe = evaluatorRepository.findById(id);
        if(maybe.isEmpty()) return Optional.empty();
        Evaluator existing = maybe.get();
        EvaluatorMapper.updateEntityFromDto(existing, dto);
        Evaluator saved = evaluatorRepository.save(existing);
        return Optional.of(saved);
    }

    /**
     * Delete evaluator by id. Returns true if an entity was deleted, false if none existed.
     */
    public boolean deleteById(Long id){
        if(!evaluatorRepository.existsById(id)) return false;
        evaluatorRepository.deleteById(id);
        return true;
    }
}
