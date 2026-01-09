package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.scientificWork.EventSummaryDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkResponseDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkShortDTO;
import com.example.profisbackend.dto.scientificWork.ScientificWorkSummaryDTO;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
    public ScientificWorkSummaryDTO convertToSummaryDTO(ScientificWork scientificWork) {
        if (scientificWork == null) return null;

        Long id = scientificWork.getId();

        String firstName = null;
        String lastName = null;
        if (scientificWork.getStudent() != null) {
            firstName = scientificWork.getStudent().getFirstName();
            lastName = scientificWork.getStudent().getLastName();
        }

        String title = scientificWork.getTitle();

        String studyProgramTitle = null;
        if (scientificWork.getStudyProgram() != null) {
            studyProgramTitle = scientificWork.getStudyProgram().getTitle();
        }

        String referentName = null;
        if (scientificWork.getMainEvaluator() != null) {
            String fn = scientificWork.getMainEvaluator().getFirstName();
            String ln = scientificWork.getMainEvaluator().getLastName();
            referentName = (fn != null ? fn : "") + (fn != null && ln != null ? " " : "") + (ln != null ? ln : "");
            if (referentName.isBlank()) referentName = null;
        }

        String coReferentName = null;
        if (scientificWork.getSecondEvaluator() != null) {
            String fn = scientificWork.getSecondEvaluator().getFirstName();
            String ln = scientificWork.getSecondEvaluator().getLastName();
            coReferentName = (fn != null ? fn : "") + (fn != null && ln != null ? " " : "") + (ln != null ? ln : "");
            if (coReferentName.isBlank()) coReferentName = null;
        }

        //calculating two grades from four
        Integer referentWork = scientificWork.getMainEvaluatorWorkMark();
        Integer referentCol = scientificWork.getMainEvaluatorColloquiumMark();
        Integer referentGrade = null;
        if (referentWork != null && referentCol != null) {
            referentGrade = (referentWork + referentCol) / 2;
        } else if (referentWork != null) {
            referentGrade = referentWork;
        } else if (referentCol != null) {
            referentGrade = referentCol;
        }

        Integer coWork = scientificWork.getSecondEvaluatorWorkMark();
        Integer coCol = scientificWork.getSecondEvaluatorColloquiumMark();
        Integer coReferentGrade = null;
        if (coWork != null && coCol != null) {
            coReferentGrade = (coWork + coCol) / 2;
        } else if (coWork != null) {
            coReferentGrade = coWork;
        } else if (coCol != null) {
            coReferentGrade = coCol;
        }

        List<EventSummaryDTO> events = Collections.emptyList();
        try {
            if (id != null) {
                events = eventService.getAllEventsForScientificWorkByScientificWorkId(id)
                        .stream()
                        .map(e -> new EventSummaryDTO(
                                e.getEventType() != null ? formatEventName(e.getEventType()) : null,
                                e.getEventDate()
                        ))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // fallback to empty list on any issue
        }

        return new ScientificWorkSummaryDTO(
                id,
                firstName,
                lastName,
                title,
                studyProgramTitle,
                referentName,
                coReferentName,
                referentGrade,
                coReferentGrade,
                events
        );
    }

    private String formatEventName(EventType eventType) {
        if (eventType == null) return null;
        return switch (eventType) {
            case PROPOSAL -> "Vorschlag";
            case DISCUSSION -> "Diskussion";
            case FINAL_SUBMISSION -> "Finale Abgabe";
            case REVIEW -> "Begutachtung";
            case ARCHIVE -> "Archivierung";
            case ABORT -> "Abbruch";
            /*
            unsure on how to implement this.
            case COLLOQUIUM -> "Kollegium";
            case PRESENTATION -> "Präsentation";
             */
        };
    }
}
