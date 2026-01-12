package com.example.profisbackend.repository;

import com.example.profisbackend.entities.Event;
import com.example.profisbackend.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> getEventByScientificWork_Id(Long scientificWorkId);

    List<Event> getEventsByEventDateGreaterThanEqual(LocalDate eventDateIsGreaterThan);

    void deleteAllByScientificWork_Id(Long scientificWorkId);

    List<Event> findEventsByEventTypeAndScientificWork_Id(EventType eventType, Long scientificWorkId);
}
