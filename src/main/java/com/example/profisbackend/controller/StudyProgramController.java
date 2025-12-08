package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.service.StudyProgramService;

import lombok.RequiredArgsConstructor;
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
}
