package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.profisbackend.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
