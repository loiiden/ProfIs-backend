package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.dto.event.EventShortResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkForReferentViewDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkShortDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkSwsReportDTO;
import com.example.profisbackend.entities.Event;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        List<EventResponseDTO> events = eventService
                .getAllEventsForScientificWorkByScientificWorkId(scientificWork.getId())
                .stream().map(EventMapper::toEventResponseDTO).toList();

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
                status,
                events
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


    public static ScientificWorkSwsReportDTO convertToSwsReportDTO(ScientificWork scientificWork) {
        String studentFullName = "";
        Long studentStudentNumber = null;
        if(scientificWork.getStudent() != null) {
            if (scientificWork.getStudent().getFirstName() != null) {
                studentFullName = scientificWork.getStudent().getFirstName();
            }
            if (scientificWork.getStudent().getLastName() != null) {
                studentFullName = studentFullName + " " + scientificWork.getStudent().getLastName();
            }
            studentStudentNumber = scientificWork.getStudent().getStudentNumber();
        }
        String studyProgramTitle = "";
        double sws = 0.0;
        if(scientificWork.getStudyProgram() != null) {
            if (scientificWork.getStudyProgram().getDegreeType() != null) {
                studyProgramTitle = scientificWork.getStudyProgram().getDegreeType().getLabel() + " ";
            }
            studyProgramTitle = studyProgramTitle + scientificWork.getStudyProgram().getTitle();
            sws = scientificWork.getStudyProgram().getSws();
        }
        return new ScientificWorkSwsReportDTO(
                scientificWork.getTitle(),
                studentFullName,
                studentStudentNumber,
                studyProgramTitle,
                sws
        );
    }
}
