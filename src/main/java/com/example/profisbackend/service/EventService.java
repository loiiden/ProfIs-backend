package com.example.profisbackend.service;

import com.example.profisbackend.dto.event.EventCreateDTO;
import com.example.profisbackend.dto.event.EventPatchDTO;
import com.example.profisbackend.entities.Event;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ScientificWorkService scientificWorkService;
    public List<Event> getAllEventsForScientificWorkByScientificWorkId(Long scientificWorkId) {
        return eventRepository.getEventByScientificWork_Id(scientificWorkId);
    }
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Event not found. Id: " + id));
    }
    public Event createEvent(EventCreateDTO eventCreateDTO) {
        Event event = new Event();
        event.setEventType(eventCreateDTO.eventType());
        event.setEventDate(eventCreateDTO.eventDate());
        ScientificWork scientificWork = scientificWorkService.findById(eventCreateDTO.scientificWorkId());
        event.setScientificWork(scientificWork);
        return eventRepository.save(event);
    }
    public Event updateEvent(Long id, EventPatchDTO eventPatchDTO) {
        Event event = findEventById(id);
        event.setEventDate(eventPatchDTO.eventDate());
        event.setEventType(eventPatchDTO.eventType());
        return eventRepository.save(event);
    }
    public void deleteEvent(Long id) {
        Event event = findEventById(id);
        eventRepository.deleteById(id);
    }
}
