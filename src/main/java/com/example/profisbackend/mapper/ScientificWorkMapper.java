package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkShortDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScientificWorkMapper {
    private final EventService eventService;
    public ScientificWorkResponseDTO convertToResponseDTO(ScientificWork scientificWork) {
        Long studentId = null;
        Long studyProgramId = null;
        Long mainEvaluatorId = null;
        Long secondEvaluatorId = null;
        if(scientificWork.getStudent() != null) {
            studentId = scientificWork.getStudent().getId();
        }
        if(scientificWork.getStudyProgram() != null) {
            studyProgramId = scientificWork.getStudyProgram().getId();
        }
        if(scientificWork.getMainEvaluator() != null) {
            mainEvaluatorId = scientificWork.getMainEvaluator().getId();
        }
        if(scientificWork.getSecondEvaluator() != null) {
            secondEvaluatorId = scientificWork.getSecondEvaluator().getId();
        }
        EventType status = eventService.getCurrentStatusForScientificWorkByScientificWorkId(scientificWork.getId());

        return new ScientificWorkResponseDTO(
                scientificWork.getId(),
                scientificWork.getColloquium(),
                scientificWork.getColloquiumLocation(),
                scientificWork.getColloquiumDuration(),
                scientificWork.getPresentationStart(),
                scientificWork.getPresentationEnd(),
                scientificWork.getDiscussionStart(),
                scientificWork.getDiscussionEnd(),
                scientificWork.getTitle(),
                scientificWork.getStartDate(),
                scientificWork.getEndDate(),
                studentId,
                studyProgramId,
                mainEvaluatorId,
                scientificWork.getMainEvaluatorWorkMark(),
                scientificWork.getMainEvaluatorColloquiumMark(),
                secondEvaluatorId,
                scientificWork.getSecondEvaluatorWorkMark(),
                scientificWork.getSecondEvaluatorColloquiumMark(),
                scientificWork.getComment(),
                status
        );
    }
    public static ScientificWorkShortDTO convertToShortDTO(ScientificWork scientificWork) {
        String StudyProgramTitle = null;
        if(scientificWork.getStudyProgram() != null) {
            StudyProgramTitle = scientificWork.getStudyProgram().getTitle();
        }
        return new ScientificWorkShortDTO(scientificWork.getTitle(), StudyProgramTitle);
    }
}
