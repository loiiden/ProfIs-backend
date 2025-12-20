package com.example.profisbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.profisbackend.dto.studyProgram.StudyProgramCreateDTO;
import com.example.profisbackend.dto.studyProgram.StudyProgramGetDTO;
import com.example.profisbackend.exceptions.StudyProgramNotFound;
import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.repository.StudyProgramRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class StudyProgramService {
    private  final StudyProgramRepository studyProgramRepository;

    public StudyProgram createStudyProgram(StudyProgramCreateDTO studyProgramDto) {
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
    public List<StudyProgramGetDTO> getAllStudyPrograms(){
        List<StudyProgram> studyPrograms=studyProgramRepository.findAll();
        List<StudyProgramGetDTO> studyProgramDtos= new ArrayList<StudyProgramGetDTO>();
        studyPrograms.forEach(studyProgram ->{
            studyProgramDtos.add(new StudyProgramGetDTO(studyProgram.getId(),studyProgram.getTitle(),studyProgram.getSws()));
        });
        return studyProgramDtos;
    }
}
