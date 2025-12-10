package com.example.profisbackend.controller;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.mapper.ScientificWorkMapper;
import com.example.profisbackend.model.ScientificWork;
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
    @GetMapping("{id}")
    public ResponseEntity<ScientificWorkResponseDTO> getScientificWorkById(@PathVariable("id") Long id){
        return ResponseEntity.ok(ScientificWorkMapper.convertToResponseDTO(scientificWorkService.findById(id)));
    }
    @GetMapping
    public ResponseEntity<List<ScientificWorkResponseDTO>> getAllScientificWorks(){
        return ResponseEntity.ok(scientificWorkService.findAll().stream().map(scientificWork -> ScientificWorkMapper.convertToResponseDTO(scientificWork)).toList());
    }
    @PatchMapping("{id}")
    public ResponseEntity<ScientificWorkResponseDTO> patchScientificWorkById(@PathVariable Long id, @RequestBody ScientificWorkPatchDTO patchDTO){
        return ResponseEntity.ok(ScientificWorkMapper.convertToResponseDTO(scientificWorkService.patchScientificWorkById(id, patchDTO)));
    }

    @PostMapping
    public ResponseEntity<ScientificWorkResponseDTO> createScientificWork(@RequestBody ScientificWorkCreateDTO scientificWorkCreateDTO) {
        return ResponseEntity.ok(ScientificWorkMapper.convertToResponseDTO(scientificWorkService.create(scientificWorkCreateDTO)));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteScientificWorkById(@PathVariable Long id) {
        scientificWorkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
