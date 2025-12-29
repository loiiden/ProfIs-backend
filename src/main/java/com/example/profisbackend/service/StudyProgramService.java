package com.example.profisbackend.service;

import java.util.List;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.entities.StudyProgram;

import org.springframework.stereotype.Service;

import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.repository.StudyProgramRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class StudyProgramService {
    private final StudyProgramRepository studyProgramRepository;

    public StudyProgram createStudyProgram(StudyProgramDTO studyProgramDTO) {
        return studyProgramRepository.save(StudyProgramMapper.studyProgramDtoToStudyProgram(studyProgramDTO));
    }

    public StudyProgram findById(Long id) {
        return studyProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public void deleteStudyProgram(Long id) {
        studyProgramRepository.delete(findById(id));
    }

    public List<StudyProgram> findAll() {
        return studyProgramRepository.findAll();
    }
}
