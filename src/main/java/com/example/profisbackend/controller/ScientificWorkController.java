package com.example.profisbackend.controller;

import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkForReferentViewDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.mapper.EventMapper;
import com.example.profisbackend.mapper.ScientificWorkMapper;
import com.example.profisbackend.service.EventService;
import com.example.profisbackend.service.ScientificWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/scientific-work")
public class ScientificWorkController {
    private final ScientificWorkService scientificWorkService;
    private final EventService eventService;
    private final ScientificWorkMapper scientificWorkMapper;

    @GetMapping("{id}")
    public ResponseEntity<ScientificWorkResponseDTO> getScientificWorkById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scientificWorkMapper.convertToResponseDTO(scientificWorkService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ScientificWorkResponseDTO>> getAllScientificWorks() {
        return ResponseEntity.ok(scientificWorkService.findAll().stream()
                .map(scientificWork -> scientificWorkMapper.convertToResponseDTO(scientificWork)).toList());
    }

    @PatchMapping("{id}")
    public ResponseEntity<ScientificWorkResponseDTO> patchScientificWorkById(@PathVariable Long id,
            @RequestBody ScientificWorkPatchDTO patchDTO) {
        try {
            return ResponseEntity.ok(
                    scientificWorkMapper.convertToResponseDTO(scientificWorkService.patchScientificWorkById(id, patchDTO)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ScientificWorkResponseDTO> createScientificWork(
            @RequestBody ScientificWorkCreateDTO scientificWorkCreateDTO) {
        try {
            return ResponseEntity
                    .ok(scientificWorkMapper.convertToResponseDTO(scientificWorkService.create(scientificWorkCreateDTO)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteScientificWorkById(@PathVariable Long id) {
        scientificWorkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/events")
    public ResponseEntity<List<EventResponseDTO>> getEventsByScientificWorkId(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getAllEventsForScientificWorkByScientificWorkId(id).stream().map(EventMapper::toEventResponseDTO).toList()) ;
    }
    @GetMapping("/filter/mainEvaluator/{id}")
    public ResponseEntity<List<ScientificWorkForReferentViewDTO>> getScientificWorkByMainEvaluatorId(@PathVariable Long id) {
        return ResponseEntity.ok(scientificWorkService.findAllScientificWorkByMainEvaluatorId(id).stream().map(scientificWorkMapper::convertToReferentViewDTO).toList());
    }
    @GetMapping("/filter/secondEvaluator/{id}")
    public ResponseEntity<List<ScientificWorkForReferentViewDTO>> getScientificWorkBySecondEvaluatorId(@PathVariable Long id) {
        return ResponseEntity.ok(scientificWorkService.findAllScientificWorkBySecondEvaluatorId(id).stream().map(scientificWorkMapper::convertToReferentViewDTO).toList());
    }
    @GetMapping("/filter/mainOrSecondEvaluator/{id}")
    public ResponseEntity<List<ScientificWorkForReferentViewDTO>> getScientificWorkByMainOrSecondEvaluatorId(@PathVariable Long id) {
        return ResponseEntity.ok(scientificWorkService.findAllScientificWorkAsMainOrSecondEvaluatorByEvaluatorId(id).stream().map(scientificWorkMapper::convertToReferentViewDTO).toList());
    }
}
