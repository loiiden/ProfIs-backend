package com.example.profisbackend.dto.event;

import com.example.profisbackend.enums.EventType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventCreateDTO(
        @NotNull
        EventType eventType,
        @NotNull
        LocalDate eventDate,
        @NotNull
        Long scientificWorkId
) {}
