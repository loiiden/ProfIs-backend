package com.example.profisbackend.service;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.entities.StudyProgram;
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

    public com.example.profisbackend.entities.ScientificWork findById(Long id) {
        return scientificWorkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Scientific Work Not Found. ID: " + id));
    }

    public ScientificWork create(ScientificWorkCreateDTO scientificWorkCreateDTO) {
        ScientificWork scientificWork = new ScientificWork();
        scientificWork.setColloquium(scientificWorkCreateDTO.colloquium());
        scientificWork.setColloquiumLocation(scientificWorkCreateDTO.colloquiumLocation());
        scientificWork.setColloquiumDuration(scientificWorkCreateDTO.colloquiumDuration());
        scientificWork.setPresentationStart(scientificWorkCreateDTO.presentationStart());
        scientificWork.setPresentationEnd(scientificWorkCreateDTO.presentationEnd());
        scientificWork.setDiscussionStart(scientificWorkCreateDTO.discussionStart());
        scientificWork.setDiscussionEnd(scientificWorkCreateDTO.discussionEnd());
        scientificWork.setTitle(scientificWorkCreateDTO.title());
        scientificWork.setStartDate(scientificWorkCreateDTO.startDate());
        scientificWork.setEndDate(scientificWorkCreateDTO.endDate());

        if (scientificWorkCreateDTO.studentId() != null) {
            Student student = studentService.findStudentById(scientificWorkCreateDTO.studentId());
            scientificWork.setStudent(student);
            student.getScientificWorks().add(scientificWork);
        }

        if(scientificWorkCreateDTO.studyProgramId() != null) {
            StudyProgram studyProgram = studyProgramService.findById(scientificWorkCreateDTO.studyProgramId());
            scientificWork.setStudyProgram(studyProgram);
        }

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

        scientificWork.setMainEvaluatorWorkMark(scientificWorkCreateDTO.mainEvaluatorWorkMark());
        scientificWork.setMainEvaluatorColloquiumMark(scientificWorkCreateDTO.mainEvaluatorColloquiumMark());
        scientificWork.setSecondEvaluatorWorkMark(scientificWorkCreateDTO.secondEvaluatorWorkMark());
        scientificWork.setSecondEvaluatorColloquiumMark(scientificWorkCreateDTO.secondEvaluatorColloquiumMark());


        scientificWorkRepository.save(scientificWork);

        return scientificWork;
    }

    public ScientificWork patchScientificWorkById(Long id, ScientificWorkPatchDTO scientificWorkPatchDTO) {
        //TODO: think if it's needed to check if fields are nulls, maybe just handle it like "put" and not "patch".
        ScientificWork scientificWork = findById(id);
        scientificWork.setColloquium(scientificWorkPatchDTO.colloquium());
        scientificWork.setColloquiumLocation(scientificWorkPatchDTO.colloquiumLocation());
        scientificWork.setColloquiumDuration(scientificWorkPatchDTO.colloquiumDuration());
        scientificWork.setPresentationStart(scientificWorkPatchDTO.presentationStart());
        scientificWork.setPresentationEnd(scientificWorkPatchDTO.presentationEnd());
        scientificWork.setDiscussionStart(scientificWorkPatchDTO.discussionStart());
        scientificWork.setDiscussionEnd(scientificWorkPatchDTO.discussionEnd());
        scientificWork.setTitle(scientificWorkPatchDTO.title());
        scientificWork.setStartDate(scientificWorkPatchDTO.startDate());
        scientificWork.setEndDate(scientificWorkPatchDTO.endDate());
        if (scientificWorkPatchDTO.studentId() != null) {
            Student newStudent = studentService.findStudentById(scientificWorkPatchDTO.studentId());
            scientificWork.setStudent(newStudent);
        }else{
            scientificWork.setStudent(null);
        }
        if (scientificWorkPatchDTO.studyProgramId() != null) {
            StudyProgram newStudyProgram = studyProgramService
                    .findById(scientificWorkPatchDTO.studyProgramId());
            scientificWork.setStudyProgram(newStudyProgram);
        }else{
            scientificWork.setStudyProgram(null);
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

        scientificWork.setMainEvaluatorWorkMark(scientificWorkPatchDTO.mainEvaluatorWorkMark());
        scientificWork.setMainEvaluatorColloquiumMark(scientificWorkPatchDTO.mainEvaluatorColloquiumMark());
        scientificWork.setSecondEvaluatorWorkMark(scientificWorkPatchDTO.secondEvaluatorWorkMark());
        scientificWork.setSecondEvaluatorColloquiumMark(scientificWorkPatchDTO.secondEvaluatorColloquiumMark());

        scientificWorkRepository.save(scientificWork);
        return scientificWork;
    }

    public void deleteById(Long id) {
        ScientificWork scientificWork = findById(id);
        scientificWorkRepository.deleteById(scientificWork.getId());
    }


    public List<ScientificWork> findAll() {
        return scientificWorkRepository.findAll();
    }
}
