package com.example.lab1.service;

import com.example.lab1.dto.request.VenueReferenceRequest;
import com.example.lab1.dto.request.VenueRequest;
import com.example.lab1.dto.response.VenueResponse;
import com.example.lab1.entity.Venue;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.VenueMapper;
import com.example.lab1.repository.TicketRepository;
import com.example.lab1.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository repository;
    private final TicketRepository ticketRepository;
    private final VenueMapper mapper;

    public VenueService(
            VenueRepository repository,
            TicketRepository ticketRepository,
            VenueMapper mapper
    ) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VenueResponse getById(Integer id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public VenueResponse create(VenueRequest request) {
        Venue venue = createEntity(request);
        return mapper.toResponse(venue);
    }

    @Transactional
    public VenueResponse update(Integer id, VenueRequest request) {
        Venue venue = find(id);

        venue.setName(request.name());
        venue.setCapacity(request.capacity());
        venue.setType(request.venueType());

        return mapper.toResponse(venue);
    }

    @Transactional
    public void delete(Integer id, Integer replacementId) {
        Venue venue = find(id);
        var tickets = ticketRepository.findAllByVenue_Id(id);

        if (!tickets.isEmpty()) {
            ReferenceRequestValidator.requireReplacement(replacementId, "Venue");
            ReferenceRequestValidator.requireDifferent(id, replacementId, "Venue");

            Venue replacement = find(replacementId);
            tickets.forEach(ticket -> ticket.setVenue(replacement));
        }

        repository.delete(venue);
    }

    @Transactional
    public Venue resolve(VenueReferenceRequest request) {
        ReferenceRequestValidator.requireExactlyOne(
                request.id(),
                request.newObject(),
                "venue"
        );

        return request.id() != null
                ? find(request.id())
                : createEntity(request.newObject());
    }

    private Venue createEntity(VenueRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    private Venue find(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue с id=" + id + " не найден"
                        )
                );
    }
}
