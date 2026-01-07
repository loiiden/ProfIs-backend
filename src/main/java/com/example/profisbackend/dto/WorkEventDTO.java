package com.example.profisbackend.dto;

import java.time.LocalDateTime;

public record WorkEventDTO(
        String eventType,
        LocalDateTime eventDate
) {
}

