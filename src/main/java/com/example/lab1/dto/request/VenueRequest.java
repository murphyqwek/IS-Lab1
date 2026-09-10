package com.example.lab1.dto.request;

import com.example.lab1.entity.VenueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VenueRequest(
     @NotNull @NotBlank String name,
     @NotNull @Positive Integer capacity,
     VenueType venueType
) {

}