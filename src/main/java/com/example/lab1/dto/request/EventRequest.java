package com.example.lab1.dto.request;

import com.example.lab1.entity.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
        @NotNull @NotBlank String name,
        @NotNull String description,
        EventType eventType
) {
}
