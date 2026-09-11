package com.example.lab1.service;

import com.example.lab1.dto.request.EventReferenceRequest;
import com.example.lab1.dto.request.EventRequest;
import com.example.lab1.dto.response.EventResponse;
import com.example.lab1.entity.Event;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.EventMapper;
import com.example.lab1.repository.EventRepository;
import com.example.lab1.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;
    private final TicketRepository ticketRepository;
    private final EventMapper mapper;

    public EventService(
            EventRepository repository,
            TicketRepository ticketRepository,
            EventMapper mapper
    ) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getById(Integer id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        Event event = createEntity(request);
        return mapper.toResponse(event);
    }

    @Transactional
    public EventResponse update(Integer id, EventRequest request) {
        Event event = find(id);

        event.setName(request.name());
        event.setDescription(request.description());
        event.setEventType(request.eventType());

        return mapper.toResponse(event);
    }

    @Transactional
    public void delete(Integer id, Integer replacementId) {
        Event event = find(id);
        var tickets = ticketRepository.findAllByEvent_Id(id);

        if (!tickets.isEmpty()) {
            ReferenceRequestValidator.requireReplacement(replacementId, "Event");
            ReferenceRequestValidator.requireDifferent(id, replacementId, "Event");

            Event replacement = find(replacementId);
            tickets.forEach(ticket -> ticket.setEvent(replacement));
        }

        repository.delete(event);
    }

    @Transactional
    public Event resolve(EventReferenceRequest request) {
        ReferenceRequestValidator.requireExactlyOne(
                request.id(),
                request.newObject(),
                "event"
        );

        return request.id() != null
                ? find(request.id())
                : createEntity(request.newObject());
    }

    private Event createEntity(EventRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    private Event find(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event с id=" + id + " не найден"
                        )
                );
    }
}
