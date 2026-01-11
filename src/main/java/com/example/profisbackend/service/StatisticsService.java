package com.example.profisbackend.service;

import com.example.profisbackend.derivedStructures.ScientificWorkMarkDistribution;
import com.example.profisbackend.dto.components.MarksComponentHomePageDTO;
import com.example.profisbackend.dto.reports.SwsReportDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkSwsReportDTO;
import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.Event;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.mapper.ScientificWorkMapper;
import com.example.profisbackend.utils.SemesterUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            Event status = eventService.getCurrentStatusForScientificWorkByScientificWorkId(work.getId());
            if (status != null && status.getEventType() != EventType.ARCHIVE){
                numberOfOpenWorks++;
            }
        }
        for(ScientificWork work: evaluator.getScientificWorksAsSecondEvaluator()){
            Event status = eventService.getCurrentStatusForScientificWorkByScientificWorkId(work.getId());
            if (status != null && status.getEventType() != EventType.ARCHIVE){
                numberOfOpenWorks++;
            }
        }
        return numberOfOpenWorks;
    }


    public Double getSwsInCurrentSemesterForEvaluatorByEvaluator(Evaluator evaluator){
        String currentSemester = SemesterUtility.getSemesterByDate(LocalDate.now());
        return getSwsInGivenSemesterForEvaluator(evaluator, currentSemester);
    }


    public Double getSwsInCurrentSemesterForEvaluatorByEvaluatorId(Long evaluatorId){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        return getSwsInCurrentSemesterForEvaluatorByEvaluator(evaluator);
    }


    public Double getSwsInGivenSemesterForEvaluatorByEvaluatorId(Long evaluatorId, String semester){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        return getSwsInGivenSemesterForEvaluator(evaluator, semester);
    }


//TODO: refactor this thing. Semesters should be an attributes of work (saved in db) and not be calculated to runtime
    public List<ScientificWork> getScientificWorksByEvaluatorAndSemester(Evaluator evaluator, String semester){
        List<ScientificWork> result = new ArrayList<>();
        List<ScientificWork> scientificWorksAsMainEvaluator = evaluator.getScientificWorksAsMainEvaluator();
        for(ScientificWork scientificWork :  scientificWorksAsMainEvaluator) {
            if (scientificWorkService.isScientificWorkHasSemester(scientificWork, semester)){
                //Scientific work is allowed not to have any study program
                result.add(scientificWork);
            }
        }
        return result;
    }
    public List<ScientificWork> getScientificWorksByEvaluatorAndSemester(Long evaluatorId, String semester){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        return getScientificWorksByEvaluatorAndSemester(evaluator, semester);
    }

    public SwsReportDTO getSwsReportByEvaluatorAndSemester(Evaluator evaluator, String semester){
        List<ScientificWorkSwsReportDTO> works = getScientificWorksByEvaluatorAndSemester(evaluator, semester)
                .stream().map(ScientificWorkMapper::convertToSwsReportDTO)
                .toList();
        String evaluatorFullName = "";
        if (evaluator.getFirstName() != null){
            evaluatorFullName = evaluatorFullName + evaluator.getFirstName();
        }
        if (evaluator.getLastName() != null){
            evaluatorFullName = evaluatorFullName + " " + evaluator.getLastName();
        }
        return new SwsReportDTO(
                evaluatorFullName,
                semester,
                works,
                getSwsInGivenSemesterForEvaluator(evaluator, semester)
        );
    }


    public SwsReportDTO getSwsReportByEvaluatorAndSemester(Long evaluatorId, String semester){
        Evaluator evaluator = evaluatorService.findById(evaluatorId);
        return getSwsReportByEvaluatorAndSemester(evaluator, semester);
    }


    public Optional<SwsReportDTO> getSwsReportForMainUserBySemester(String semester){
        Optional<Evaluator> evaluator = evaluatorService.findMainUser();
        if (evaluator.isPresent()){
            return Optional.of(getSwsReportByEvaluatorAndSemester(evaluator.get(), semester));
        }
        return Optional.empty();
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


    public Double getSwsInCurrentSemesterForMainUser(){
        Optional<Evaluator> mainUser = evaluatorService.findMainUser();
        if (mainUser.isEmpty()){
            return 0.0;
        }
        Evaluator evaluator = mainUser.get();
        return getSwsInCurrentSemesterForEvaluatorByEvaluator(evaluator);
    }


    public Double getSwsInGivenSemesterForMainUser(String semester){
        Optional<Evaluator> mainUser = evaluatorService.findMainUser();
        if (mainUser.isEmpty()){
            return 0.0;
        }
        Evaluator evaluator = mainUser.get();
        return getSwsInGivenSemesterForEvaluator(evaluator, semester);
    }


    public ScientificWorkMarkDistribution getScientificWorkMarkDistribution(){
        int countWorksWithMarkFromOneToTwo = 0;
        int countWorksWithMarkFromTwoToThree = 0;
        int countWorksWithMarkFromThreeToFour = 0;
        int countWorksWithMarkFromFourToFive = 0;
        List<ScientificWork> scientificWorks = scientificWorkService.findAll();
        for (ScientificWork scientificWork: scientificWorks){
            Optional<Double> averageMarkOptional = scientificWorkService.getAverageScoreInGermanSystem(scientificWork);
            if (averageMarkOptional.isPresent()){
                double averageMark = averageMarkOptional.get();
                if (averageMark >= 1.0 && averageMark < 2.0){
                    countWorksWithMarkFromOneToTwo++;
                } else if (averageMark >= 2.0 && averageMark < 3.0){
                    countWorksWithMarkFromTwoToThree++;
                } else if (averageMark >= 3.0 && averageMark < 4.0){
                    countWorksWithMarkFromThreeToFour++;
                } else if (averageMark >= 4.0 && averageMark <= 5.0){
                    countWorksWithMarkFromFourToFive++;
                }
            }
        }
        return new ScientificWorkMarkDistribution(
                countWorksWithMarkFromOneToTwo,
                countWorksWithMarkFromTwoToThree,
                countWorksWithMarkFromThreeToFour,
                countWorksWithMarkFromFourToFive
        );
    }
    public MarksComponentHomePageDTO getDataForMarksComponentHomePage(){
        Optional<Double> averageMarkForAllScientificWorksOptional = scientificWorkService.getAverageMarkForAllWorks();
        Double averageMarkForAllScientificWorks = null;
        if (averageMarkForAllScientificWorksOptional.isPresent()){
            averageMarkForAllScientificWorks = averageMarkForAllScientificWorksOptional.get();
        }
        return new MarksComponentHomePageDTO(
                eventService.getAllScientificWorksForStatusOfEventType(EventType.ARCHIVE).size(),
                eventService.getAllNotArchivedScientificWorks().size(),
                averageMarkForAllScientificWorks,
                getScientificWorkMarkDistribution()
        );
    }
}
