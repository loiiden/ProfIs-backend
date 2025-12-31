package com.example.profisbackend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.repository.ScientificWorkRepository;

@ExtendWith(MockitoExtension.class)
public class ScientificWorkServiceTest {
    @Mock
    ScientificWorkRepository scientificWorkRepository;
    @Mock
    StudentService studentService;
    @Mock
    EvaluatorService evaluatorService;
    @Mock
    StudyProgramService studyProgramService;
    @InjectMocks
    ScientificWorkService scientificWorkService;
    @Captor
    ArgumentCaptor<ScientificWork> scientificWorkCaptor;

    @Test
    void saveScientificWorkOnce() {
        // Arrange
        LocalDateTime colloquium = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        String location = "Frankfurt";
        Duration duration = Duration.ofHours(1);
        LocalTime presentationStart = LocalTime.of(2, 0);
        LocalTime presentationEnd = LocalTime.of(3, 0);
        LocalTime discussionStart = LocalTime.of(4, 0);
        LocalTime discussionEnd = LocalTime.of(5, 0);
        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 1);
        Long studentId = 559617L;
        Long studyProgramId = 333L;
        Long mainEvaluatorId = 333L;
        int mainEvaluatorWorkMark = 55;
        int mainEvaluatorColloquiumMark = 55;
        Long secondEvaluatorId = 334L;
        int secondEvaluatorWorkMark = 55;
        int secondEvaluatorColloquiumMark = 55;
        ScientificWorkCreateDTO scientificWorkCreateDTO = new ScientificWorkCreateDTO(colloquium, location, duration, presentationStart, presentationEnd, discussionStart, discussionEnd, "Quantum Physics",
                startDate, endDate, studentId, studyProgramId, mainEvaluatorId, mainEvaluatorWorkMark, mainEvaluatorColloquiumMark, secondEvaluatorId, secondEvaluatorWorkMark, secondEvaluatorColloquiumMark);

        Student expectedStudent = new Student();
        expectedStudent.setId(studentId);
        expectedStudent.setStudentNumber(1L);
        expectedStudent.setAcademicLevel(AcademicLevel.NONE);
        expectedStudent.setScientificWorks(new ArrayList<ScientificWork>());

        StudyProgram expectedStudyProgram = new StudyProgram(scientificWorkCreateDTO.studyProgramId(),
                "Computer Science", 0.1f);

        Evaluator expectedMainEvaluator = new Evaluator();
        expectedMainEvaluator.setId(mainEvaluatorId);
        Evaluator expectedSecondEvaluator = new Evaluator();
        expectedSecondEvaluator.setId(secondEvaluatorId);
        when(studentService.findStudentById(studentId))
                .thenReturn(expectedStudent);
        when(studyProgramService.findById(expectedStudyProgram.getId()))
                .thenReturn(expectedStudyProgram);
        when(evaluatorService.findById(expectedMainEvaluator.getId()))
                .thenReturn(expectedMainEvaluator);
        when(evaluatorService.findById(expectedSecondEvaluator.getId()))
                .thenReturn(expectedSecondEvaluator);
        when(scientificWorkRepository.save(any(ScientificWork.class)))
                .thenReturn(new ScientificWork());

        // Act
        scientificWorkService.create(scientificWorkCreateDTO);
        verify(scientificWorkRepository, times(1)).save(scientificWorkCaptor.capture());
        // Assert
        verify(scientificWorkRepository, times(1)).save(scientificWorkCaptor.getValue());
        Assertions.assertEquals(scientificWorkCreateDTO.title(), scientificWorkCaptor.getValue().getTitle());
        Assertions.assertEquals(scientificWorkCreateDTO.studyProgramId(),
                scientificWorkCaptor.getValue().getStudyProgram().getId());
        Assertions.assertEquals(scientificWorkCreateDTO.studentId(),
                scientificWorkCaptor.getValue().getStudent().getId());
    }
}