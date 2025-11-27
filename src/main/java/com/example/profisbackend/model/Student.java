package com.example.profisbackend.model;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student extends Person  {
    @Id
    private Long studentNumber;
    
    @OneToMany(mappedBy ="student")
    private List<ScientificWork>scientificWorks;
}
