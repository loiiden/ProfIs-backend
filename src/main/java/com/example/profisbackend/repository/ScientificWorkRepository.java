package com.example.profisbackend.repository;

import com.example.profisbackend.model.ScientificWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScientificWorkRepository extends JpaRepository<ScientificWork, Long> {
}
