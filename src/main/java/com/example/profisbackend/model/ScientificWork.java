package com.example.profisbackend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.ManyToOne;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Data
@Entity
public class ScientificWork {
    @Id
    private Long id;
    private LocalDateTime colloquium;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    private Student student;
    @ManyToMany
    private List<Evaluator> evaluators;
    @OneToMany(mappedBy ="scientificWork" )
    private List<Mark> marks;
    @OneToOne
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;
    
}
