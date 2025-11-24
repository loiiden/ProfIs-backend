package com.example.profisbackend.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Evaluator extends Person{
    @Id 
    private Long id;
    @ManyToMany(mappedBy = "evaluators")
    private List<ScientificWork> scientificWorks;
    @OneToMany(mappedBy ="evaluator" )
    private List<Mark> marks;

}
