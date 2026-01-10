package com.example.profisbackend.dto.event;

import com.example.profisbackend.enums.EventType;

import java.time.LocalDate;

public record EventShortResponseDTO(EventType eventType, LocalDate eventDate) {
}
