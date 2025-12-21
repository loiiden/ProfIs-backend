package com.example.profisbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.profisbackend.dto.studyprogram.StudyProgramDto;
import com.example.profisbackend.dto.studyprogram.StudyProgramResponseDTO;
import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.service.StudyProgramService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/study-program")
public class StudyProgramController {
    private final StudyProgramService studyProgramService;
    @PostMapping
    public ResponseEntity<StudyProgram>createStudyProgram(@RequestBody StudyProgramDto studyProgramDto){
        return ResponseEntity.ok().body(studyProgramService.createStudyProgram(studyProgramDto));
    } 
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteStudyProgram(@PathVariable Long id){
        studyProgramService.deleteStudyProgram(id);
        return ResponseEntity.ok().body("Deleted StudyProgram: "+id);
    }
    @GetMapping()
    public ResponseEntity<List<StudyProgramResponseDTO>> getMethodName() {
        return ResponseEntity.ok(studyProgramService.getAllStudyPrograms().stream().map(StudyProgramMapper::convertToStudyProgramResponseDTO).collect(Collectors.toList()));
    }
}
