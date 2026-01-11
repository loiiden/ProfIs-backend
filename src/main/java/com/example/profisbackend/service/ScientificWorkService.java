package com.example.profisbackend.service;

import com.example.profisbackend.dto.scientificWork.ScientificWorkCreateDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkPatchDTO;
import com.example.profisbackend.entities.*;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.mapper.MarkMapper;
import com.example.profisbackend.repository.ScientificWorkRepository;
import com.example.profisbackend.utils.SemesterUtility;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
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
        scientificWork.setComment(scientificWorkCreateDTO.comment());


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
        scientificWork.setComment(scientificWorkPatchDTO.comment());

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


    //this property is being recalculated each time it's used.
    //If you use it so much, that it decreases performance, it's sane to store it inside scientific work as a field.
    public List<String> getSemestersOfScientificWork(ScientificWork scientificWork) {
        LocalDate startDate = scientificWork.getStartDate();
        LocalDate endDate = scientificWork.getEndDate();
        return SemesterUtility.getAllSemestersBetweenTwoDates(startDate, endDate);
    }


    public List<String> getSemestersOfScientificWorkById(Long id) {
        ScientificWork scientificWork = findById(id);
        return getSemestersOfScientificWork(scientificWork);
    }

    public Boolean isScientificWorkHasSemester(ScientificWork scientificWork, String semester){
        if (!semester.matches("(WS|SS)\\d{4}")){
            throw new IllegalArgumentException("Illegal semester argument in provided");
        }
        return (getSemestersOfScientificWork(scientificWork).contains(semester));
    }
    public boolean existsByStartDateAndStudent(LocalDate startDate, Long studentId) {
        return scientificWorkRepository.existsByStartDateAndStudentId(startDate, studentId);
    }

    public ScientificWork findByStartDateAndStudent(LocalDate startDate, Long studentId) {
        return scientificWorkRepository.findByStartDateAndStudentId(startDate, studentId).orElseThrow(() -> new EntityNotFoundException("ScientificWork not found  by  startdate and studentId "+ startDate+" "+studentId));
    }

    public List<ScientificWork> findAllScientificWorkByMainEvaluatorId(Long mainEvaluatorId) {
        return scientificWorkRepository.getScientificWorksByMainEvaluator_Id(mainEvaluatorId);
    }


    public List<ScientificWork> findAllScientificWorkBySecondEvaluatorId(Long secondEvaluatorId) {
        return scientificWorkRepository.getScientificWorksBySecondEvaluator_Id(secondEvaluatorId);
    }


    public List<ScientificWork> findAllScientificWorkAsMainOrSecondEvaluatorByEvaluatorId(Long evaluatorId) {
        List<ScientificWork> result = new ArrayList<>();
        result.addAll(findAllScientificWorkByMainEvaluatorId(evaluatorId));
        result.addAll(findAllScientificWorkBySecondEvaluatorId(evaluatorId));
        return result;
    }


    public Optional<Double> getAverageScore(ScientificWork scientificWork) {
        int count = 0;
        double totalScore = 0;
        if (scientificWork.getMainEvaluatorWorkMark() != null) {
            totalScore += scientificWork.getMainEvaluatorWorkMark();
            count++;
        }
        if (scientificWork.getMainEvaluatorColloquiumMark() != null) {
            totalScore += scientificWork.getMainEvaluatorColloquiumMark();
            count++;
        }
        if (scientificWork.getSecondEvaluatorWorkMark() != null) {
            totalScore += scientificWork.getSecondEvaluatorWorkMark();
            count++;
        }
        if (scientificWork.getSecondEvaluatorColloquiumMark() != null) {
            totalScore += scientificWork.getSecondEvaluatorColloquiumMark();
            count++;
        }
        if (count == 0){
            return Optional.empty();
        }
        return Optional.of(totalScore / count);
    }


    public Optional<Double> getAverageScoreInGermanSystem(ScientificWork scientificWork) {
        Optional<Double> averageScore = getAverageScore(scientificWork);
        if (averageScore.isPresent()){
            return Optional.of(MarkMapper.pointsToGermanNote(averageScore.get()));
        }
        return Optional.empty();
    }


    public Optional<Double> getAverageMarkForAllWorks(){
        double totalScore = 0.0;
        int count = 0;
        List<ScientificWork> scientificWorks = findAll();
        for (ScientificWork scientificWork : scientificWorks) {
            Optional<Double> averageScoreInGermanSystem = getAverageScoreInGermanSystem(scientificWork);
            if (averageScoreInGermanSystem.isPresent()){
                totalScore = totalScore + averageScoreInGermanSystem.get();
                count++;
            }
        }
        if (count == 0){
            return Optional.empty();
        }
        return Optional.of(totalScore/count);
    }
    public  void deleteAllScientificWorks(){
        scientificWorkRepository.deleteAll();
        log.info("Scientificworks have been deleted");
    }
}
