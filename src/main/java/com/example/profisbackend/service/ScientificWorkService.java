package com.example.profisbackend.service;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.model.Evaluator;
import com.example.profisbackend.model.ScientificWork;
import com.example.profisbackend.model.Student;
import com.example.profisbackend.model.StudyProgram;
import com.example.profisbackend.repository.ScientificWorkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScientificWorkService {
    private final ScientificWorkRepository scientificWorkRepository;
    private final StudentService studentService;
    private final StudyProgramService studyProgramService;
    private final EvaluatorService evaluatorService;

    public ScientificWork findById(Long id) {
        return scientificWorkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Scientific Work Not Found. ID: " + id));
    }

    public ScientificWork create(ScientificWorkCreateDTO scientificWorkCreateDTO) {
        ScientificWork scientificWork = new ScientificWork();
        scientificWork.setColloquium(scientificWorkCreateDTO.colloquium());
        scientificWork.setTitle(scientificWorkCreateDTO.title());
        scientificWork.setStartDate(scientificWorkCreateDTO.startDate());
        scientificWork.setEndDate(scientificWorkCreateDTO.endDate());

        // Student must already exit it will be retrieved from the database
        Student student = studentService.getStudentById(scientificWorkCreateDTO.studentId());
        // A Scientificwork contains a Student
        scientificWork.setStudent(student);
        // One Student can have mutliple scientificworks
        student.getScientificWorks().add(scientificWork);

        if(scientificWorkCreateDTO.mainEvaluatorId() != null) {
            Evaluator mainEvaluator = evaluatorService.findById(scientificWorkCreateDTO.mainEvaluatorId());
            scientificWork.setMainEvaluator(mainEvaluator);
            mainEvaluator.getScientificWorksAsMainEvaluator().add(scientificWork);
        }
        if(scientificWorkCreateDTO.secondEvaluatorId() != null) {
            Evaluator secondEvaluator = evaluatorService.findById(scientificWorkCreateDTO.secondEvaluatorId());
            scientificWork.setSecondEvaluator(secondEvaluator);
            secondEvaluator.getScientificWorksAsSecondEvaluator().add(scientificWork);
        }

        StudyProgram studyProgram = studyProgramService.getStudyProgramById(scientificWorkCreateDTO.studyProgramId());

        scientificWork.setStudyProgram(studyProgram);
        scientificWorkRepository.save(scientificWork);

        return scientificWork;
    }

    public ScientificWork patchScientificWorkById(Long id, ScientificWorkPatchDTO scientificWorkPatchDTO) {
        ScientificWork scientificWork = findById(id);
        if (scientificWorkPatchDTO.colloquium() != null) {
            scientificWork.setColloquium(scientificWorkPatchDTO.colloquium());
        }
        if (scientificWorkPatchDTO.title() != null) {
            scientificWork.setTitle(scientificWorkPatchDTO.title());
        }
        if (scientificWorkPatchDTO.startDate() != null) {
            scientificWork.setStartDate(scientificWorkPatchDTO.startDate());
        }
        if (scientificWorkPatchDTO.endDate() != null) {
            scientificWork.setEndDate(scientificWorkPatchDTO.endDate());
        }
        if (scientificWorkPatchDTO.studentId() != null) {
            Student newStudent = studentService.getStudentById(scientificWorkPatchDTO.studentId());
            scientificWork.setStudent(newStudent);
        }
        if (scientificWorkPatchDTO.studyProgramId() != null) {
            StudyProgram newStudyProgram = studyProgramService
                    .getStudyProgramById(scientificWorkPatchDTO.studyProgramId());
            scientificWork.setStudyProgram(newStudyProgram);
        }

        if(scientificWorkPatchDTO.mainEvaluatorId() != null) {
            if (scientificWork.getMainEvaluator() != null) {
                Evaluator currentMainEvaluator =  scientificWork.getMainEvaluator();
                currentMainEvaluator.getScientificWorksAsMainEvaluator().remove(scientificWork);
            };
            scientificWork.setMainEvaluator(evaluatorService.findById(scientificWorkPatchDTO.mainEvaluatorId()));
        }else{
            if (scientificWork.getMainEvaluator() != null) {
                Evaluator currentMainEvaluator =  scientificWork.getMainEvaluator();
                currentMainEvaluator.getScientificWorksAsMainEvaluator().remove(scientificWork);
            };
            scientificWork.setMainEvaluator(null);
        }

        if(scientificWorkPatchDTO.secondEvaluatorId() != null) {
            if (scientificWork.getSecondEvaluator() != null) {
                Evaluator currentSecondEvaluator =  scientificWork.getSecondEvaluator();
                currentSecondEvaluator.getScientificWorksAsSecondEvaluator().remove(scientificWork);
            };
            scientificWork.setSecondEvaluator(evaluatorService.findById(scientificWorkPatchDTO.secondEvaluatorId()));
        }else{
            if (scientificWork.getSecondEvaluator() != null) {
                Evaluator currentSecondEvaluator =  scientificWork.getSecondEvaluator();
                currentSecondEvaluator.getScientificWorksAsSecondEvaluator().remove(scientificWork);
            };
            scientificWork.setSecondEvaluator(null);
        }

        scientificWorkRepository.save(scientificWork);
        return scientificWork;
    }

    public void deleteById(Long id) {
        ScientificWork scientificWork = findById(id);
        scientificWorkRepository.deleteById(scientificWork.getId());
    }

    /*
     * DO IT IN NOTE SERVICE
     * public void addNote()
     */
    public List<ScientificWork> findAll() {
        return scientificWorkRepository.findAll();
    }
}
