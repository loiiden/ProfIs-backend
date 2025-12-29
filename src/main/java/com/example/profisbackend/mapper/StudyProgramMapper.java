package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.dto.studyprogram.StudyProgramResponseDTO;
import com.example.profisbackend.entities.StudyProgram;

public class StudyProgramMapper {
    public static StudyProgram studyProgramDtoToStudyProgram(StudyProgramDTO studyProgramDto) {
        return StudyProgram.builder()
                .title(studyProgramDto.title())
                .sws(studyProgramDto.sws())
                .build();
    }

    public static StudyProgramResponseDTO convertToStudyProgramResponseDTO(StudyProgram studyProgram) {
        return new StudyProgramResponseDTO(studyProgram.getId(), studyProgram.getTitle(), studyProgram.getSws());
    }
}
