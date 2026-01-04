package com.example.profisbackend.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long studentNumber;
    @OneToMany(mappedBy = "student")
    private List<ScientificWork> scientificWorks = new ArrayList<>();

    @ManyToOne
    private StudyProgram studyProgram;
}
