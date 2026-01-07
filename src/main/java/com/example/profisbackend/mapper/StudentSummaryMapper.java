package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.StudentSummaryDTO;
import com.example.profisbackend.dto.ScientificWorkSummaryDTO;
import com.example.profisbackend.dto.WorkEventDTO;
import com.example.profisbackend.enums.MarkType;
import com.example.profisbackend.model.Mark;
import com.example.profisbackend.model.ScientificWork;
import com.example.profisbackend.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentSummaryMapper {

    public static StudentSummaryDTO toSummary(Student student) {
        List<ScientificWorkSummaryDTO> works = student.getScientificWorks().stream()
                .map(StudentSummaryMapper::toWorkSummary)
                .sorted(Comparator.comparing(ScientificWorkSummaryDTO::id))
                .collect(Collectors.toList());

        return new StudentSummaryDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getStudentNumber(),
                works
        );
    }

    private static ScientificWorkSummaryDTO toWorkSummary(ScientificWork work) {
        // Build events: we expose marks as events with their type and an approximate date (colloquium uses the colloquium date)
        List<WorkEventDTO> events = new ArrayList<>();

        // If colloquium date exists, add an event
        if (work.getColloquium() != null) {
            events.add(new WorkEventDTO("COLLOQUIUM_SCHEDULED", work.getColloquium()));
        }

        // Convert marks into events. The Mark model stores the id as an embedded id; we expose type name
        if (work.getMarks() != null) {
            for (Mark mark : work.getMarks()) {
                // If mark has composedId and type, use it. We defensively handle nulls.
                if (mark.getComposedId() != null && mark.getComposedId().getType() != null) {
                    String type = mark.getComposedId().getType().name();
                    // We don't have a timestamp on marks; use colloquium if available, otherwise use startDate at start of day
                    LocalDateTime date = work.getColloquium() != null ? work.getColloquium() : (work.getStartDate() != null ? work.getStartDate().atStartOfDay() : null);
                    events.add(new WorkEventDTO(type, date));
                }
            }
        }

        // Aggregate marks into two numbers for frontend: scientificWork average and colloquium average
        Double avgSW = averageScoreByType(work.getMarks(), MarkType.SCIENTIFICWORK);
        Double avgCol = averageScoreByType(work.getMarks(), MarkType.COLLOQUIUM);

        return new ScientificWorkSummaryDTO(
                work.getId(),
                work.getTitle(),
                work.getStartDate(),
                work.getEndDate(),
                work.getColloquium(),
                events.stream().sorted(Comparator.comparing(WorkEventDTO::eventDate, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList()),
                avgSW,
                avgCol
        );
    }

    private static Double averageScoreByType(List<Mark> marks, MarkType type) {
        if (marks == null || marks.isEmpty()) return null;
        int sum = 0;
        int count = 0;
        for (Mark m : marks) {
            if (m.getComposedId() == null || m.getComposedId().getType() == null) continue;
            if (m.getComposedId().getType() == type) {
                sum += m.getScore();
                count++;
            }
        }
        if (count == 0) return null;
        return sum / (double) count;
    }
}
