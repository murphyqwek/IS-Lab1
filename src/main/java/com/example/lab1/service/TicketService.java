package com.example.lab1.service;

import com.example.lab1.dto.request.TicketRequest;
import com.example.lab1.dto.response.TicketResponse;
import com.example.lab1.entity.Ticket;
import com.example.lab1.mapper.TicketMapper;
import com.example.lab1.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public void save(TicketRequest ticket) {
        var ticketEntity = ticketMapper.toEntity(ticket);
        ticketRepository.save(ticketEntity);
    }

    @Transactional
    public TicketResponse getById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Ticket with id " + id + " not found"
                        )
                );

        return ticketMapper.toResponse(ticket);
    }
}
