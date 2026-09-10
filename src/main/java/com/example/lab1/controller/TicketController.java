package com.example.lab1.controller;

import com.example.lab1.dto.request.TicketRequest;
import com.example.lab1.dto.response.TicketResponse;
import com.example.lab1.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> GetTicketById(@PathVariable int id) {
        var ticket = ticketService.getById(id);

        if(ticket == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> CreateTicket(@RequestBody @Valid TicketRequest ticketRequest) {
        var ticket = ticketService.save(ticketRequest);

        if(ticket == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ticket);
    }

}
