package com.example.profisbackend.model;
import java.util.List;

import com.example.profisbackend.enums.EvaluatorRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public  class Evaluator extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy ="evaluator" )
    private List<Mark> marks;
    private EvaluatorRole role;

    @OneToMany(mappedBy = "mainEvaluator")
    private List<ScientificWork> scientificWorksAsMainEvaluator;

    @OneToMany(mappedBy = "secondEvaluator")
    private List<ScientificWork> scientificWorksAsSecondEvaluator;
}
