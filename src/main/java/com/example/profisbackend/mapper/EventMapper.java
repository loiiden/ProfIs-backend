package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.dto.event.EventShortResponseDTO;
import com.example.profisbackend.entities.Event;

public class EventMapper {
    public static EventResponseDTO toEventResponseDTO(Event event){
        if (event == null){
            return null;
        }
        return new EventResponseDTO(
                event.getId(),
                event.getEventType(),
                event.getEventDate(),
                event.getScientificWork().getId()
        );
    }
    public static EventShortResponseDTO toEventShortResponseDTO(Event event){
        if (event == null){
            return null;
        }
        return new EventShortResponseDTO(
                event.getEventType(),
                event.getEventDate()
        );
    }
}
