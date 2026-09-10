package com.example.lab1.mapper;

import com.example.lab1.dto.request.TicketRequest;
import com.example.lab1.dto.response.TicketResponse;
import com.example.lab1.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    private final CoordinatesMapper coordinatesMapper;
    private final EventMapper eventMapper;
    private final PersonMapper personMapper;
    private final VenueMapper venueMapper;

    public TicketMapper(CoordinatesMapper coordinatesMapper, EventMapper eventMapper, PersonMapper personMapper, VenueMapper venueMapper) {
        this.coordinatesMapper = coordinatesMapper;
        this.eventMapper = eventMapper;
        this.personMapper = personMapper;
        this.venueMapper = venueMapper;
    }

    public Ticket toEntity(TicketRequest request) {
        Ticket ticket = new Ticket();

        ticket.setName(request.name());
        ticket.setCoordinates(coordinatesMapper.toEntity(request.coordinatesRequest()));
        ticket.setDiscount(request.discount());
        ticket.setEvent(eventMapper.toEntity(request.eventRequest()));
        ticket.setPerson(personMapper.toEntity(request.personRequest()));
        ticket.setVenue(venueMapper.toEntity(request.venueRequest()));
        ticket.setNumber(request.number());
        ticket.setPrice(request.price());
        ticket.setType(request.ticketType());

        return ticket;
    }

    public TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getName(),
                coordinatesMapper.toResponse(ticket.getCoordinates()),
                ticket.getCreationDate(),
                personMapper.toResponse(ticket.getPerson()),
                eventMapper.toResponse(ticket.getEvent()),
                ticket.getPrice(),
                ticket.getType(),
                ticket.getDiscount(),
                ticket.getNumber(),
                venueMapper.toResponse(ticket.getVenue())
        );
    }
}