package com.example.profisbackend.service;

import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final EvaluatorService evaluatorService;
    private final EventService eventService;

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

}
