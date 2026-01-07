package com.example.profisbackend.dto.event;

import com.example.profisbackend.enums.EventType;

import java.time.LocalDate;

public record EventPatchDTO(EventType eventType, LocalDate eventDate) {
}
