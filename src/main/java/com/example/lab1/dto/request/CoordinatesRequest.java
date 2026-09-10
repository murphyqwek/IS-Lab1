package com.example.lab1.dto.request;

import jakarta.validation.constraints.NotNull;

public record CoordinatesRequest(
        @NotNull Double x,
        @NotNull Double y
        ) {
}
