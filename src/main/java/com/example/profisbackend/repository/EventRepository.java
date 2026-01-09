package com.example.profisbackend.repository;

import com.example.profisbackend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getEventByScientificWork_Id(Long scientificWorkId);

    List<Event> getEventsByEventDateGreaterThanEqual(LocalDate eventDateIsGreaterThan);
}
