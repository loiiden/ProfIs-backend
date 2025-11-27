package com.example.profisbackend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class StudyProgram {
    @Id
    private Long id;
    private String title;
    private float sws;   
}
