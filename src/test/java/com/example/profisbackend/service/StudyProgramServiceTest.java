package com.example.profisbackend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.profisbackend.dto.StudyProgramDto;
import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.repository.StudyProgramRepository;

@ExtendWith(MockitoExtension.class)
public class StudyProgramServiceTest {
    @Mock
    StudyProgramRepository studyProgramRepository;
    @InjectMocks // inject the mocks into the referenced class automatically
    StudyProgramService studyProgramService;

    @Test
    void retunStudyProgram() {
        // Arrange
        StudyProgramDto studyProgramDto = new StudyProgramDto("Computer Science", 0.1f);
        StudyProgram expectedProgram = StudyProgramMapper.studyProgramDtoToStudyProgram(studyProgramDto);
        when(studyProgramRepository.save(any(StudyProgram.class)))
                .thenReturn(expectedProgram);
        // Act
        studyProgramService.createStudyProgram(studyProgramDto);
        // Assert
        verify(studyProgramRepository, times(1)).save(expectedProgram);
        Assertions.assertEquals("Computer Science", expectedProgram.getTitle());
        Assertions.assertEquals(0.1f, expectedProgram.getSws());
    }

    @Test
    void studyProgramFoundById() {
        //Arrange 
        Long id=1L;
        StudyProgram studyProgram= StudyProgram.builder()
        .id(id)
        .title("Computer Science")
        .sws(0.1f)
        .build();
        Optional<StudyProgram> expectedProgram= Optional.of(studyProgram);
        when(studyProgramRepository.findById(id))
        .thenReturn(expectedProgram);
        //Act
        studyProgramService.deleteStudyProgram(id);
        //Assert
        verify(studyProgramRepository,times(1)).findById(id);
    }
//Todo 

}
