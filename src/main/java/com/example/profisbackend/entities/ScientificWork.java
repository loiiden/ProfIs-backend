package com.example.profisbackend.entities;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ScientificWork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime colloquium;
    private String colloquiumLocation;
    private Duration colloquiumDuration;
    private LocalTime presentationStart;
    private LocalTime presentationEnd;
    private LocalTime discussionStart;
    private LocalTime discussionEnd;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private Student student;


    @ManyToOne
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;

    @ManyToOne
    private Evaluator mainEvaluator;

    @Min(0)
    @Max(100)
    private Integer mainEvaluatorWorkMark;

    @Min(0)
    @Max(100)
    private Integer mainEvaluatorColloquiumMark;


    @ManyToOne
    private Evaluator secondEvaluator;

    @Min(0)
    @Max(100)
    private Integer secondEvaluatorWorkMark;

    @Min(0)
    @Max(100)
    private Integer secondEvaluatorColloquiumMark;

    String comment;
}
