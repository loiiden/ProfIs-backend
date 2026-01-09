package com.example.profisbackend.service;

import com.example.profisbackend.dto.event.EventCreateDTO;
import com.example.profisbackend.dto.event.EventPatchDTO;
import com.example.profisbackend.entities.Event;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.enums.EventType;
import com.example.profisbackend.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ScientificWorkService scientificWorkService;

    public List<Event> getAllEventsForScientificWorkByScientificWorkId(Long scientificWorkId) {
        List<Event> events = eventRepository.getEventByScientificWork_Id(scientificWorkId);
        events.sort(Comparator.comparing(Event::getEventDate));
        return events;
    }


    public EventType getCurrentStatusForScientificWorkByScientificWorkId(Long scientificWorkId) {
        LocalDate today = LocalDate.now();
        List<Event> events = getAllEventsForScientificWorkByScientificWorkId(scientificWorkId);
        for(Event event : events.reversed()){
            if (event.getEventDate().isBefore(today) || event.getEventDate().isEqual(today)){
                return event.getEventType();
            }
        }
        return null;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }


    public List<Event> getNextEvents(){
        List<Event> nextEvents = eventRepository.getEventsByEventDateGreaterThanEqual(LocalDate.now());
        nextEvents.sort(Comparator.comparing(Event::getEventDate));
        return nextEvents;
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
