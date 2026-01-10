package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.ScientificWork;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;

@Repository
public interface ScientificWorkRepository extends JpaRepository<ScientificWork, Long> {
    boolean existsByStartDateAndStudentId(LocalDate startDate, Long StudentId);

    Optional<ScientificWork> findByStartDateAndStudentId(LocalDate startDate, Long StudentId);

    List<ScientificWork> getScientificWorksByMainEvaluator_Id(Long mainEvaluatorId);
    List<ScientificWork> getScientificWorksBySecondEvaluator_Id(Long secondEvaluatorId);
}
