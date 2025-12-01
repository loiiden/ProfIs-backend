package com.example.profisbackend.service;

import org.springframework.stereotype.Service;

import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.repository.StudyProgramRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class StudyProgramService {
    private  final StudyProgramRepository studyProgramRepository;

    public StudyProgram createStudyProgram(StudyProgramDto studyProgramDto) {
       return studyProgramRepository.save(StudyProgramMapper.studyProgramDtoToStudyProgram(studyProgramDto));
    }
}
