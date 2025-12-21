package com.example.profisbackend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.profisbackend.dto.studyprogram.StudyProgramDto;
import com.example.profisbackend.dto.studyprogram.StudyProgramResponseDTO;
import com.example.profisbackend.model.StudyProgram;

public class StudyProgramMapper {
    public static StudyProgram studyProgramDtoToStudyProgram(StudyProgramDto studyProgramDto) {
        return StudyProgram.builder()
                .title(studyProgramDto.title())
                .sws(studyProgramDto.sws())
                .build();
    }

    public static StudyProgramResponseDTO convertToStudyProgramResponseDTO(StudyProgram studyProgram) {
        return new StudyProgramResponseDTO(studyProgram.getId(), studyProgram.getTitle(), studyProgram.getSws());
    }
}
