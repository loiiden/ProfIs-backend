package com.example.profisbackend.model;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Referent entity represents an Evaluator who serves as a referent.
 *
 * Inheritance:
 * - Person (mapped superclass) provides personal fields (firstName, lastName, email, etc.)
 * - Evaluator (entity) provides the primary key `id` and role/marks relationship
 *
 * This entity intentionally adds no new fields; it exists to represent the concrete
 * subtype in the domain model and to allow polymorphic queries if needed.
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Referent extends Evaluator {
    // id is defined in Evaluator (mapped in the Evaluator entity)
    // Use inherited fields from Person (firstName, lastName, etc.)
    // No additional fields required here for now.
}
