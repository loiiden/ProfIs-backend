package com.example.profisbackend.mapper;
import com.example.profisbackend.dto.studyProgram.StudyProgramCreateDTO;
import com.example.profisbackend.model.StudyProgram;

public class StudyProgramMapper {
    public static StudyProgram studyProgramDtoToStudyProgram(StudyProgramCreateDTO studyProgramDto){
        return StudyProgram.builder()
       .title(studyProgramDto.title())
       .sws(studyProgramDto.sws())
       .build();
    }   
}
//TODO test void method with argumentcaptor (read documentation on Baeldung)