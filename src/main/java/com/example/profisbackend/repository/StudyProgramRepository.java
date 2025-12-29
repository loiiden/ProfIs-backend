package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.StudyProgram;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long> {
}