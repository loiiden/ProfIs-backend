package com.example.profisbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.service.StudyProgramService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class StudyProgramController {
    private final StudyProgramService studyProgramService;
    @PostMapping
    public ResponseEntity<StudyProgram>createStudyProgram(@RequestBody StudyProgramDto studyProgramDto){
        return ResponseEntity.ok().body(studyProgramService.createStudyProgram(studyProgramDto));
    }   
}
