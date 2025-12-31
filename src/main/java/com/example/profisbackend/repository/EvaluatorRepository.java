package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.Evaluator;

/**
 * Spring Data JPA repository for Evaluator entities.
 * Provides standard CRUD operations and can be extended with custom queries.
 */
@Repository
public interface EvaluatorRepository extends JpaRepository<Evaluator, Long> {
}