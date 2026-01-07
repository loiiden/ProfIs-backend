package com.example.profisbackend.service;

import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.utils.SemesterUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final EvaluatorService evaluatorService;
    private final EventService eventService;
    private final ScientificWorkService scientificWorkService;

    public Integer getNumberOfOpenWorksByEvaluatorId(Long evaluatorId){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        int numberOfOpenWorks = 0;
        for(ScientificWork work: evaluator.getScientificWorksAsMainEvaluator()){
            if (eventService.getCurrentStatusForScientificWorkByScientificWorkId(work.getId()) != EventType.ARCHIVE){
                numberOfOpenWorks++;
            }
        }
        for(ScientificWork work: evaluator.getScientificWorksAsSecondEvaluator()){
            if (eventService.getCurrentStatusForScientificWorkByScientificWorkId(work.getId()) != EventType.ARCHIVE){
                numberOfOpenWorks++;
            }
        }
        return numberOfOpenWorks;
    }


    public Double getSwsInCurrentSemesterForEvaluatorByEvaluatorId(Long evaluatorId){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        String currentSemester = SemesterUtility.getSemesterByDate(LocalDate.now());
        return getSwsInGivenSemesterForEvaluator(evaluator, currentSemester);
    }


    public Double getSwsInGivenSemesterForEvaluatorByEvaluatorId(Long evaluatorId, String semester){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        return getSwsInGivenSemesterForEvaluator(evaluator, semester);
    }


    public Double getSwsInGivenSemesterForEvaluator(Evaluator evaluator, String semester){
        double SWS = (double) 0;
        for(ScientificWork scientificWork : evaluator.getScientificWorksAsMainEvaluator() ) {
            if (scientificWorkService.isScientificWorkHasSemester(scientificWork, semester)){
                //if work has no study program we don't throw anything. Might be changed if needed.
                //If we want to create more predictable version, this should throw error
                //And in general not be allowed to create work without study program
                //For dev purposes it's ok
                if (scientificWork.getStudyProgram() != null){
                    SWS = SWS + scientificWork.getStudyProgram().getSws();
                }
            }
        }
        return SWS;
    }
}
