package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.StudentSummaryDTO;
import com.example.profisbackend.dto.ScientificWorkSummaryDTO;
import com.example.profisbackend.dto.WorkEventDTO;
import com.example.profisbackend.enums.MarkType;
import com.example.profisbackend.model.Mark;
import com.example.profisbackend.model.ScientificWork;
import com.example.profisbackend.model.Student;
import com.example.profisbackend.model.embeddable.MarkId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentSummaryMapperTest {

    @Test
    void toSummary_convertsStudentWithWorkAndMarks() {
        Student s = new Student();
        s.setId(1L);
        s.setFirstName("Max");
        s.setLastName("Mustermann");
        s.setStudentNumber(12345L);

        ScientificWork w = new ScientificWork();
        w.setId(10L);
        w.setTitle("Thesis");
        w.setStartDate(LocalDate.of(2024,1,1));
        w.setColloquium(LocalDateTime.of(2024,6,1,10,0));
        w.setStudent(s);

        // Create two marks: one scientific work mark and one colloquium mark
        Mark m1 = new Mark();
        MarkId id1 = new MarkId();
        // Use reflection to set private fields of MarkId because it has no constructor
        // but MarkId getters exist; set via reflection for tests only
        try {
            java.lang.reflect.Field typeField = MarkId.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(id1, MarkType.SCIENTIFICWORK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        m1.setComposedId(id1);
        m1.setScore(80);
        m1.setScientificWork(w);

        Mark m2 = new Mark();
        MarkId id2 = new MarkId();
        try {
            java.lang.reflect.Field typeField = MarkId.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(id2, MarkType.COLLOQUIUM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        m2.setComposedId(id2);
        m2.setScore(60);
        m2.setScientificWork(w);

        List<Mark> marks = new ArrayList<>();
        marks.add(m1);
        marks.add(m2);
        w.setMarks(marks);

        List<ScientificWork> works = new ArrayList<>();
        works.add(w);
        s.setScientificWorks(works);

        StudentSummaryDTO summary = StudentSummaryMapper.toSummary(s);

        assertThat(summary).isNotNull();
        assertThat(summary.id()).isEqualTo(1L);
        assertThat(summary.scientificWorks()).hasSize(1);

        ScientificWorkSummaryDTO ws = summary.scientificWorks().get(0);
        assertThat(ws.id()).isEqualTo(10L);
        assertThat(ws.title()).isEqualTo("Thesis");
        assertThat(ws.avgScientificWorkScore()).isEqualTo(80.0);
        assertThat(ws.avgColloquiumScore()).isEqualTo(60.0);
        assertThat(ws.events()).isNotEmpty();
        boolean hasColloquiumScheduled = ws.events().stream().anyMatch(e -> "COLLOQUIUM_SCHEDULED".equals(e.eventType()));
        assertThat(hasColloquiumScheduled).isTrue();
    }
}

