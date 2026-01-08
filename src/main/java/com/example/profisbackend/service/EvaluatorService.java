package com.example.profisbackend.service;

import com.example.profisbackend.dto.evaluator.EvaluatorPatchDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.dto.evaluator.EvaluatorCreateDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.profisbackend.mapper.EvaluatorMapper;
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
     * Create and persist a Evaluator from a create DTO.
     * @param createDto DTO with data for the new Evaluator (id is not present).
     * @return persisted Evaluator entity with generated id.
     */
    public Evaluator createEvaluator(EvaluatorCreateDTO createDto) {
        Evaluator toSave = EvaluatorMapper.toEntity(createDto);
        return evaluatorRepository.save(toSave);
    }

    /**
     * Find a Evaluator by id.
     * @param id primary key
     * @return Evaluator entity
     */
    public Evaluator findById(Long id){
        return evaluatorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluator Not Found. ID: " + id));
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
    public Evaluator patchEvaluator(Long id, EvaluatorPatchDTO dto){
        Evaluator evaluator = findById(id);
        EvaluatorMapper.updateEntityFromDto(evaluator, dto);
        evaluatorRepository.save(evaluator);
        return evaluator;
    }

    /**
     * Delete evaluator by id. Returns true if an entity was deleted, false if none existed.
     */
    public void deleteById(Long id){
        Evaluator toDelete = findById(id);
        evaluatorRepository.deleteById(toDelete.getId());
    }

    public  Boolean existsByEmail(String mail){
       return evaluatorRepository.existsByEmail(mail);
    }
    public Evaluator findByEmail(String mail){
        return evaluatorRepository.findByEmail(mail).orElseThrow(()-> new EntityNotFoundException("Evaluator not found with "+mail));
    }
}
