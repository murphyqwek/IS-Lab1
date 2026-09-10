package com.example.lab1.dto.request;

import com.example.lab1.entity.TicketType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record TicketRequest(
        @NotNull @NotBlank String name,
        @NotNull @Valid CoordinatesRequest coordinatesRequest,
        @Valid PersonRequest personRequest,
        @NotNull @Valid EventRequest eventRequest,
        @Positive @NotNull Integer price,
        TicketType ticketType,
        @Min(1) @Max(100) @NotNull Integer discount,
        @Positive Float number,
        @Valid VenueRequest venueRequest
) {
}