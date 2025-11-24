package com.example.profisbackend.model;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import com.example.profisbackend.enums.MarkType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
@Entity
public class Mark {
    @Id
    private Long id;
    @Min(0)
    @Max(100)
    private int score;
    private MarkType type;
    @ManyToOne
    private ScientificWork scientificWork;
    @ManyToOne
    private Evaluator evaluator;

}
