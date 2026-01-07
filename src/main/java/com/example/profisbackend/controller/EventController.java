package com.example.profisbackend.controller;

import com.example.profisbackend.dto.event.EventCreateDTO;
import com.example.profisbackend.dto.event.EventPatchDTO;
import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.mapper.EventMapper;
import com.example.profisbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/event")
public class EventController {
    private final EventService eventService;
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreateDTO eventCreateDTO) {
        return ResponseEntity.ok(EventMapper.toEventResponseDTO(eventService.createEvent(eventCreateDTO)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(EventMapper.toEventResponseDTO(eventService.findEventById(id)));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDTO> patchEvent(@PathVariable Long id, @RequestBody EventPatchDTO eventPatchDTO) {
        return ResponseEntity.ok(EventMapper.toEventResponseDTO(eventService.updateEvent(id, eventPatchDTO)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EventResponseDTO> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
