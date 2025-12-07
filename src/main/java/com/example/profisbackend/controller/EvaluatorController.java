package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.profisbackend.dto.EvaluatorDto;
import com.example.profisbackend.mapper.EvaluatorMapper;
import com.example.profisbackend.model.Evaluator;
import com.example.profisbackend.service.EvaluatorService;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EvaluatorController exposes a minimal REST API to manage Evaluator entities.
 *
 * Endpoints:
 * - POST  /Evaluator       : Create a new Evaluator from a EvaluatorDto JSON body.
 *                           Returns 201 Created with the created EvaluatorDto and Location header /Evaluator/{id}.
 * - GET   /Evaluator/{id}  : Retrieve a Evaluator by id. Returns 200 with EvaluatorDto or 404 if not found.
 * - GET   /Evaluator       : Retrieve all evaluators.
 * - PATCH /Evaluator/{id}  : Partially update an evaluator.
 * - DELETE /Evaluator/{id} : Delete an evaluator by id.
 *
 * DTO shape (EvaluatorDto): { id, firstName, lastName, email, phoneNumber, academicLevel, role }
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/evaluator")
public class EvaluatorController {
    private final EvaluatorService evaluatorService;

    /**
     * Create a Evaluator.
     * Request body: EvaluatorDto JSON (id can be null for creation).
     * Response: 201 Created with saved EvaluatorDto in the body and Location header pointing to /Evaluator/{id}.
     */
    @PostMapping
    public ResponseEntity<EvaluatorDto> createEvaluator(@RequestBody EvaluatorDto evaluatorDto){
        Evaluator created = evaluatorService.createEvaluator(evaluatorDto);
        EvaluatorDto dto = EvaluatorMapper.toDto(created);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /**
     * Retrieve a Evaluator by id.
     * Path variable: id (Long)
     * Response: 200 OK with EvaluatorDto if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluatorDto> getEvaluator(@PathVariable Long id){
        Evaluator found = evaluatorService.findById(id);
        if(found == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EvaluatorMapper.toDto(found));
    }

    /**
     * Retrieve all evaluators.
     */
    @GetMapping
    public ResponseEntity<List<EvaluatorDto>> getAll(){
        List<Evaluator> all = evaluatorService.findAll();
        List<EvaluatorDto> dtos = all.stream().map(EvaluatorMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Partially update an existing evaluator by id.
     * The DTO's non-null fields will be applied. Returns 200 with updated DTO or 404 if not found.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EvaluatorDto> patchEvaluator(@PathVariable Long id, @RequestBody EvaluatorDto dto){
        var updated = evaluatorService.patchEvaluator(id, dto);
        if(updated.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EvaluatorMapper.toDto(updated.get()));
    }

    /**
     * Delete an evaluator by id. Returns 204 No Content if deleted, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluator(@PathVariable Long id){
        boolean deleted = evaluatorService.deleteById(id);
        if(!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
