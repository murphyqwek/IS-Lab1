package com.example.lab1.service;

import com.example.lab1.dto.filter.TicketFilter;
import com.example.lab1.dto.filter.TicketSortField;
import com.example.lab1.dto.request.TicketRequest;
import com.example.lab1.dto.response.TicketResponse;
import com.example.lab1.entity.Ticket;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.TicketMapper;
import com.example.lab1.repository.TicketRepository;
import com.example.lab1.specification.TicketSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CoordinatesService coordinatesService;
    private final PersonService personService;
    private final EventService eventService;
    private final VenueService venueService;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, CoordinatesService coordinatesService, PersonService personService, EventService eventService, VenueService venueService, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.coordinatesService = coordinatesService;
        this.personService = personService;
        this.eventService = eventService;
        this.venueService = venueService;
        this.ticketMapper = ticketMapper;
    }

    @Transactional(readOnly = true)
    public TicketResponse getById(Integer id) {
        return ticketMapper.toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> getAll(TicketFilter filter, int page, int size, TicketSortField sortBy, Sort.Direction direction) {
        Specification<Ticket> specification = Specification.unrestricted();

        if (filter.name() != null && !filter.name().isBlank()) {
            specification = specification.and(
                    TicketSpecification.nameEquals(
                            filter.name()
                    )
            );
        }

        if (filter.eventName() != null && !filter.eventName().isBlank()) {
            specification = specification.and(
                    TicketSpecification.eventNameEquals(
                            filter.eventName()
                    )
            );
        }

        if (filter.eventDescription() != null && !filter.eventDescription().isBlank()) {
            specification = specification.and(
                    TicketSpecification.eventDescriptionEquals(
                            filter.eventDescription()
                    )
            );
        }

        if (filter.venueName() != null && !filter.venueName().isBlank()) {
            specification = specification.and(
                    TicketSpecification.venueNameEquals(
                            filter.venueName()
                    )
            );
        }

        Sort sort = Sort.by(direction, sortBy.getProperty());

        Pageable pageable = PageRequest.of(page, size, sort);

        return ticketRepository.findAll(specification, pageable).map(ticketMapper::toResponse);
    }

    @Transactional
    public TicketResponse create(TicketRequest request) {
        Ticket ticket = new Ticket();
        applyRequest(ticket, request);

        ticketRepository.save(ticket);

        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse update(Integer id, TicketRequest request) {
        Ticket ticket = find(id);

        applyRequest(ticket, request);

        // save() не нужен: ticket managed, Hibernate применит dirty checking.
        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public void delete(Integer id) {
        ticketRepository.delete(find(id));
    }

    private void applyRequest(Ticket ticket, TicketRequest request) {
        ticket.setName(request.name());
        ticket.setCoordinates(coordinatesService.resolve(request.coordinates()));
        ticket.setEvent(eventService.resolve(request.event()));

        ticket.setPerson(
                request.person() == null
                        ? null
                        : personService.resolve(request.person())
        );

        ticket.setVenue(
                request.venue() == null
                        ? null
                        : venueService.resolve(request.venue())
        );

        ticket.setPrice(request.price());
        ticket.setType(request.ticketType());
        ticket.setDiscount(request.discount());
        ticket.setNumber(request.number());
    }

    private Ticket find(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket с id=" + id + " не найден"
                        )
                );
    }
}
