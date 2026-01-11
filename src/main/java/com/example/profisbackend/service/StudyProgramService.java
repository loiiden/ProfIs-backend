package com.example.profisbackend.service;

import java.util.List;
import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.enums.DegreeType;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.repository.StudyProgramRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Log4j2
@Service

public class StudyProgramService {
    private final StudyProgramRepository studyProgramRepository;

    public StudyProgram createStudyProgram(StudyProgramDTO studyProgramDTO) {
        return studyProgramRepository.save(StudyProgramMapper.studyProgramDtoToStudyProgram(studyProgramDTO));
    }

    public StudyProgram findById(Long id) {
        return studyProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Study program not found " + id));
    }

    public void deleteStudyProgram(Long id) {
        studyProgramRepository.delete(findById(id));
    }

    public List<StudyProgram> findAll() {
        return studyProgramRepository.findAll();
    }

    public Boolean existsByDegreeTypeAndTitle(DegreeType degreeType, String title) {
        return studyProgramRepository.existsByDegreeTypeAndTitle(degreeType, title);
    }
    public StudyProgram findByDegreeTypeAndTitle(DegreeType degreeType, String title) {
        return studyProgramRepository.findByDegreeTypeAndTitle(degreeType, title).orElseThrow(() -> new EntityNotFoundException("Study program not found "));
    }
    public  void deleteAllStudyPrograms(){
        studyProgramRepository.deleteAll();
        log.info("Studyprograms have been deleted");
    }
}
