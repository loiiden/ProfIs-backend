package com.example.profisbackend.service;

import java.util.Optional;

import com.example.profisbackend.exceptions.StudyProgramNotFound;
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
    public StudyProgram getStudyProgramById(Long id) throws StudyProgramNotFound {
        return studyProgramRepository.findById(id).orElseThrow(() -> new StudyProgramNotFound(id));
    }
    public void deleteStudyProgram(Long id){
        Optional<StudyProgram> optionalStudyProgram=studyProgramRepository.findById(id);
        optionalStudyProgram.ifPresentOrElse(program->
            studyProgramRepository.delete(program),
            () -> { throw new StudyProgramNotFound(id); }
        );
    }
}
