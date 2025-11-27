package com.example.profisbackend.model.embeddable;

import java.io.Serializable;

import com.example.profisbackend.enums.MarkType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EvaluationId implements Serializable {
    @Column(name = "scientific_work_id")
    private long scientificWorkId;
    @Column(name = "evaluator_id")
    private long evaluatorId;
    @Enumerated(EnumType.STRING)
    @Column(name = "mark_type")
    private MarkType markType;
}
