package com.example.profisbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.profisbackend.dto.studyProgram.StudyProgramCreateDTO;
import com.example.profisbackend.dto.studyProgram.StudyProgramGetDTO;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.service.StudyProgramService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/study-program")
public class StudyProgramController {
    private final StudyProgramService studyProgramService;
    @PostMapping
    public ResponseEntity<StudyProgram>createStudyProgram(@RequestBody StudyProgramCreateDTO studyProgramDto){
        return ResponseEntity.ok().body(studyProgramService.createStudyProgram(studyProgramDto));
    } 
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteStudyProgram(@PathVariable Long id){
        studyProgramService.deleteStudyProgram(id);
        return ResponseEntity.ok().body("Deleted StudyProgram: "+id);
    }
    @GetMapping("/all")
    public ResponseEntity<List<StudyProgramGetDTO>> getMethodName() {
        return ResponseEntity.ok(studyProgramService.getAllStudyPrograms());
    }
    
}
