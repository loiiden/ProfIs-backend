package com.example.profisbackend.model;
import java.util.List;

import com.example.profisbackend.enums.EvaluatorRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public  class Evaluator extends Person{
    @Id 
    private Long id;

    @OneToMany(mappedBy = "evaluator")
     private List<Evaluation> scientificWorks;

    @OneToMany(mappedBy ="evaluator" )
    private List<Mark> marks;
    private EvaluatorRole role;
}
