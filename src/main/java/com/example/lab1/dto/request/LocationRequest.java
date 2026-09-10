package com.example.lab1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotNull Float x,
        @NotNull Long y,
        @NotNull Long z,
        @Size(max = 692) String name
) {
}