package com.example.profisbackend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.profisbackend.enums.DegreeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.profisbackend.dto.studyprogram.StudyProgramDTO;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.mapper.StudyProgramMapper;
import com.example.profisbackend.repository.StudyProgramRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StudyProgramServiceTest {
        @Mock
        StudyProgramRepository studyProgramRepository;
        @InjectMocks // inject the mocks into the referenced class automatically
        StudyProgramService studyProgramService;

        @Test
        void studyProgramSavedOnce() {
                // Arrange
                StudyProgramDTO studyProgramDto = new StudyProgramDTO(DegreeType.B_SC, "Computer Science", 0.1f);
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
        void returnEntityNotFoundException() {
                // Arrange
                StudyProgram studyProgram = StudyProgram.builder()
                                .id(1L)
                                .title("Computer Science")
                                .sws(0.1f)
                                .build();

                when(studyProgramRepository.findById(studyProgram.getId()))
                                .thenReturn(Optional.empty());
                // Act
                assertThrows(EntityNotFoundException.class,
                                () -> studyProgramService.deleteStudyProgram(studyProgram.getId()));
                // Assert
                verify(studyProgramRepository, never()).delete(studyProgram);
        }

        @Test
        void studyProgramFoundByIdCalledOnce() {
                // Arrange
                Long id = 1L;
                StudyProgram studyProgram = StudyProgram.builder()
                                .id(id)
                                .title("Computer Science")
                                .sws(0.1f)
                                .build();
                Optional<StudyProgram> expectedProgram = Optional.of(studyProgram);
                when(studyProgramRepository.findById(id))
                                .thenReturn(expectedProgram);
                // Act
                StudyProgram returnedStudyProgram = studyProgramService.findById(id);
                // Assert
                Assertions.assertEquals(expectedProgram.get(), returnedStudyProgram);
                verify(studyProgramRepository, times(1)).findById(id);
        }

}
