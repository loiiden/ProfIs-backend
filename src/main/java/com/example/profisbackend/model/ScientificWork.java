package com.example.profisbackend.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
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
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private Student student;

    @OneToMany(mappedBy ="scientificWork")
    private List<Mark> marks;
    
    @ManyToOne
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;
}
