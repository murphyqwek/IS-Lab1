package com.example.lab1.service;

import com.example.lab1.dto.request.CoordinatesReferenceRequest;
import com.example.lab1.dto.request.CoordinatesRequest;
import com.example.lab1.dto.response.CoordinatesResponse;
import com.example.lab1.entity.Coordinates;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.CoordinatesMapper;
import com.example.lab1.repository.CoordinatesRepository;
import com.example.lab1.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoordinatesService {

    private final CoordinatesRepository repository;
    private final TicketRepository ticketRepository;
    private final CoordinatesMapper mapper;

    public CoordinatesService(
            CoordinatesRepository repository,
            TicketRepository ticketRepository,
            CoordinatesMapper mapper
    ) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CoordinatesResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CoordinatesResponse getById(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public CoordinatesResponse create(CoordinatesRequest request) {
        Coordinates coordinates = createEntity(request);
        return mapper.toResponse(coordinates);
    }

    @Transactional
    public CoordinatesResponse update(Long id, CoordinatesRequest request) {
        Coordinates coordinates = find(id);
        coordinates.setX(request.x());
        coordinates.setY(request.y());
        return mapper.toResponse(coordinates);
    }

    @Transactional
    public void delete(Long id, Long replacementId) {
        Coordinates coordinates = find(id);
        var tickets = ticketRepository.findAllByCoordinates_Id(id);

        if (!tickets.isEmpty()) {
            ReferenceRequestValidator.requireReplacement(replacementId, "Coordinates");
            ReferenceRequestValidator.requireDifferent(id, replacementId, "Coordinates");

            Coordinates replacement = find(replacementId);
            tickets.forEach(ticket -> ticket.setCoordinates(replacement));
        }

        repository.delete(coordinates);
    }

    @Transactional
    public Coordinates resolve(CoordinatesReferenceRequest request) {
        ReferenceRequestValidator.requireExactlyOne(
                request.id(),
                request.newObject(),
                "coordinates"
        );

        return request.id() != null ? find(request.id()) : createEntity(request.newObject());
    }

    private Coordinates createEntity(CoordinatesRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    private Coordinates find(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coordinates с id=" + id + " не найдены"
                        )
                );
    }
}
