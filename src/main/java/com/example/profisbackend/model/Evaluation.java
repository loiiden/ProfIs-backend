package com.example.profisbackend.model;

import com.example.profisbackend.model.embeddable.EvaluationId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Evaluation {
    
    @EmbeddedId
    private EvaluationId evaluationId;
   
    @MapsId("scientificWorkId")
    @ManyToOne
    private ScientificWork scientificWork;

    @MapsId("evaluatorId")
    @ManyToOne
    private Evaluator evaluator;
    
}
