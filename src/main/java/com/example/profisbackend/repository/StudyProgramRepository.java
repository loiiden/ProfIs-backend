package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.StudyProgram;
import com.example.profisbackend.enums.DegreeType;

import java.awt.font.OpenType;
import java.util.Optional;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long> {
    public Boolean existsByDegreeTypeAndTitle(DegreeType degreeType, String title);
    public Optional<StudyProgram> findByDegreeTypeAndTitle(DegreeType degreeType, String title);
}