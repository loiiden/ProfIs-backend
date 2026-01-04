package com.example.profisbackend.entities;

import com.example.profisbackend.enums.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    private EventType eventType;
    private LocalDate eventDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name="scientificWork_id")
    private ScientificWork scientificWork;
}
