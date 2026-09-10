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
    public TicketResponse save(TicketRequest ticket) {
        var ticketEntity = ticketMapper.toEntity(ticket);
        return ticketMapper.toResponse(ticketRepository.save(ticketEntity));
    }

    @Transactional
    public TicketResponse getById(Integer id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);

        if(ticket == null) {
            return null;
        }

        return ticketMapper.toResponse(ticket);
    }
}
