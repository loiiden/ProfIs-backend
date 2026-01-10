package com.example.profisbackend.entities;
import com.example.profisbackend.enums.DegreeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder()
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "degree_type", "title" }) }) // doesn't work yet
public class StudyProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private DegreeType degreeType;
    private String title;
    private double sws;
}
