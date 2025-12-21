package com.example.profisbackend.mapper;
import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.model.StudyProgram;

public class StudyProgramMapper {
    public static StudyProgram studyProgramDtoToStudyProgram(StudyProgramDto studyProgramDto){
        return StudyProgram.builder()
       .title(studyProgramDto.title())
       .sws(studyProgramDto.sws())
       .build();
    }   
}
//TODO test void method with argumentcaptor (read documentation on Baeldung)