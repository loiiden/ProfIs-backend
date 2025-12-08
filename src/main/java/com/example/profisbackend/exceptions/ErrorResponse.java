package com.example.profisbackend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
}
