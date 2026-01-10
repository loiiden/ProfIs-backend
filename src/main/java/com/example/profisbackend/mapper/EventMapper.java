package com.example.profisbackend.mapper;

import com.example.profisbackend.dto.event.EventResponseDTO;
import com.example.profisbackend.dto.event.EventWithStudentReferenceDTO;
import com.example.profisbackend.dto.event.EventShortResponseDTO;
import com.example.profisbackend.entities.Event;
import com.example.profisbackend.entities.Student;

public class EventMapper {
    public static EventResponseDTO toEventResponseDTO(Event event){
        if (event == null){
            return null;
        }
        assert event.getScientificWork() != null;
        return new EventResponseDTO(
                event.getId(),
                event.getEventType(),
                event.getEventDate(),
                event.getScientificWork().getId()
        );
    }

    public static EventWithStudentReferenceDTO toEventWithStudentNameDTO(Event event) {
        assert event.getScientificWork() != null;
        Student student = event.getScientificWork().getStudent();
        String studentFirstName = null;
        String studentLastName = null;
        Long studentId = null;
        if (student != null) {
            studentFirstName = student.getFirstName();
            studentLastName = student.getLastName();
            studentId = student.getId();
        }
        return new EventWithStudentReferenceDTO(event.getEventType(), event.getEventDate(), event.getScientificWork().getId(), studentFirstName, studentLastName, studentId);
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
