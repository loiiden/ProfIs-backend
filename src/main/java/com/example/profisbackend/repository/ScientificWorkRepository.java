package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.ScientificWork;

@Repository
public interface ScientificWorkRepository extends JpaRepository<ScientificWork, Long> {
}
