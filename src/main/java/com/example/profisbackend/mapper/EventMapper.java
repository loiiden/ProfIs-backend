package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.entities.Event;

public class EventMapper {
    public static EventResponseDTO toEventResponseDTO(Event event){
        return new EventResponseDTO(
                event.getEventType(),
                event.getEventDate(),
                event.getScientificWork().getId()
        );
    }
}
