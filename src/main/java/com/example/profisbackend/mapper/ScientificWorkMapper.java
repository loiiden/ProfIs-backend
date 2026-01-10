package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.event.EventShortResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkForReferentViewDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkShortDTO;
import com.example.profisbackend.entities.Event;
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
        EventShortResponseDTO status = EventMapper.toEventShortResponseDTO(
                eventService.getCurrentStatusForScientificWorkByScientificWorkId(scientificWork.getId())
        );

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
    public ScientificWorkShortDTO convertToShortDTO(ScientificWork scientificWork) {
        String StudyProgramTitle = null;
        if(scientificWork.getStudyProgram() != null) {
            StudyProgramTitle = scientificWork.getStudyProgram().getTitle();
        }
        EventShortResponseDTO status  = EventMapper.toEventShortResponseDTO(eventService.getCurrentStatusForScientificWorkByScientificWorkId(scientificWork.getId()));
        return new ScientificWorkShortDTO(scientificWork.getId(), status, scientificWork.getTitle(), StudyProgramTitle);
    }

    public ScientificWorkForReferentViewDTO convertToReferentViewDTO(ScientificWork scientificWork) {
        String studyProgramTitle = null;
        if(scientificWork.getStudyProgram() != null) {
            studyProgramTitle = scientificWork.getStudyProgram().getTitle();
        }
        EventShortResponseDTO status  = EventMapper.toEventShortResponseDTO(eventService.getCurrentStatusForScientificWorkByScientificWorkId(scientificWork.getId()));
        String studentFirstName = null;
        String studentLastName = null;
        if (scientificWork.getStudent() != null) {
            studentFirstName = scientificWork.getStudent().getFirstName();
            studentLastName = scientificWork.getStudent().getLastName();
        }
        return new ScientificWorkForReferentViewDTO(scientificWork.getId(), status, studentFirstName, studentLastName, studyProgramTitle, scientificWork.getTitle());
    }
}
