package com.example.lab1.dto.request;

import com.example.lab1.entity.TicketType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record TicketRequest(
        @NotNull(message = "Поле name не может быть пустым") @NotBlank(message = "Поле name не может быть пустым") String name,
        @NotNull(message = "Поле coordinates не может быть пустым") @Valid CoordinatesRequest coordinatesRequest,
        @Valid PersonRequest personRequest,
        @NotNull(message = "Поле event не может быть пустым") @Valid EventRequest eventRequest,
        @Positive(message = "Поле price должно быть положительным числом") @NotNull(message = "Поле price не может быть пустым") Integer price,
        TicketType ticketType,
        @Min(value = 1, message = "Discount может быть в пределах от 1 до 100") @Max(value = 100, message = "Discount может быть в пределах от 1 до 100") @NotNull(message = "Поле discount не может быть пустым") Integer discount,
        @Positive(message = "Поле number не может быть пустым") Float number,
        @Valid VenueRequest venueRequest
) {
}