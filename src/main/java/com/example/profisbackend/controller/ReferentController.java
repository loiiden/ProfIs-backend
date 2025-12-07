package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.profisbackend.dto.ReferentDto;
import com.example.profisbackend.mapper.ReferentMapper;
import com.example.profisbackend.model.Referent;
import com.example.profisbackend.service.ReferentService;

import lombok.RequiredArgsConstructor;

import java.net.URI;

/**
 * ReferentController exposes a minimal REST API to manage Referent entities.
 *
 * Endpoints:
 * - POST  /referent       : Create a new Referent from a ReferentDto JSON body.
 *                           Returns 201 Created with the created ReferentDto and Location header /referent/{id}.
 * - GET   /referent/{id}  : Retrieve a Referent by id. Returns 200 with ReferentDto or 404 if not found.
 *
 * DTO shape (ReferentDto): { id, firstName, lastName, email, phoneNumber, academicLevel, role }
 * Notes:
 * - The controller uses ReferentService for persistence and ReferentMapper to convert between entity and DTO.
 * - No authentication or validation is enforced here (consider adding @Valid and security in a next step).
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/referent")
public class ReferentController {
    private final ReferentService referentService;

    /**
     * Create a Referent.
     * Request body: ReferentDto JSON (id can be null for creation).
     * Response: 201 Created with saved ReferentDto in the body and Location header pointing to /referent/{id}.
     */
    @PostMapping
    public ResponseEntity<ReferentDto> createReferent(@RequestBody ReferentDto referentDto){
        Referent created = referentService.createReferent(referentDto);
        ReferentDto dto = ReferentMapper.toDto(created);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /**
     * Retrieve a Referent by id.
     * Path variable: id (Long)
     * Response: 200 OK with ReferentDto if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferentDto> getReferent(@PathVariable Long id){
        Referent found = referentService.findById(id);
        if(found == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ReferentMapper.toDto(found));
    }
}
