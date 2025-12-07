package com.example.profisbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.profisbackend.model.Referent;

/**
 * Spring Data JPA repository for Referent entities.
 * Provides standard CRUD operations and can be extended with custom queries.
 */
@Repository
public interface ReferentRepository extends JpaRepository<Referent,Long>{
}