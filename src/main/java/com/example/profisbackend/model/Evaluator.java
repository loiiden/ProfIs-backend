package com.example.profisbackend.model;
import java.util.ArrayList;
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

    private EvaluatorRole role;

    @OneToMany(mappedBy = "mainEvaluator")
    private List<ScientificWork> scientificWorksAsMainEvaluator = new ArrayList<>();

    @OneToMany(mappedBy = "secondEvaluator")
    private List<ScientificWork> scientificWorksAsSecondEvaluator = new ArrayList<>();
}
