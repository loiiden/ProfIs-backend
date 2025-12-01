package com.example.profisbackend.model;
import com.example.profisbackend.model.embeddable.MarkId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mark {
    @EmbeddedId
    private MarkId composedId;

    @Min(0)
    @Max(100)
    private int score;

    @MapsId("scientificWorkId") // Matches the field name in MarkId
    @ManyToOne
    @JoinColumn(name = "scientific_work_id") // The FK column name in the Mark table
    private ScientificWork scientificWork;

    @MapsId("evaluatorId") // Matches the field name in MarkId
    @ManyToOne
    @JoinColumn(name = "evaluator_id") // The FK column name in the Mark table
    private Evaluator evaluator;
}
