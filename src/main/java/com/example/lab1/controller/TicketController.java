package com.example.lab1.controller;

import com.example.lab1.dto.filter.TicketFilter;
import com.example.lab1.dto.filter.TicketSortField;
import com.example.lab1.dto.request.TicketRequest;
import com.example.lab1.dto.response.TicketResponse;
import com.example.lab1.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponse>> getAll(
            TicketFilter filter,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "0")
            int size,

            @RequestParam(defaultValue = "NAME")
            TicketSortField sortBy,

            @RequestParam(defaultValue = "ASC")
            Sort.Direction direction
    ) {
        return ResponseEntity.ok(ticketService.getAll(filter, page, size, sortBy, direction));
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @Valid @RequestBody TicketRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TicketRequest request
    ) {
        return ResponseEntity.ok(ticketService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
