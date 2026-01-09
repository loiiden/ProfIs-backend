package com.example.profisbackend.dto.event;

import com.example.profisbackend.enums.EventType;

import java.time.LocalDate;

public record EventWithStudentReferenceDTO(EventType eventType, LocalDate eventDate, Long scientificWorkId, String studentFirstName, String studentLastName, Long studentId) {
}
