package com.example.lab1.service;

import com.example.lab1.dto.request.EventRequest;
import com.example.lab1.entity.Event;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.EventMapper;
import com.example.lab1.repository.EventRepository;
import com.example.lab1.websocket.ChangeType;
import com.example.lab1.websocket.EntityChangePublisher;
import com.example.lab1.websocket.EntityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EntityChangePublisher changePublisher;

    public EventService(
            EventRepository eventRepository,
            EventMapper eventMapper,
            EntityChangePublisher changePublisher
    ) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.changePublisher = changePublisher;
    }

    @Transactional
    public Event resolve(EventRequest request) {
        Event event = eventMapper.toEntity(request);
        return eventRepository.save(event);
    }

    @Transactional
    public Event update(Integer id, EventRequest request) {
        Event event = find(id);

        event.setName(request.name());
        event.setDescription(request.description());
        event.setEventType(request.eventType());

        changePublisher.publish(EntityType.EVENT, ChangeType.UPDATED, id);

        return event;
    }

    @Transactional
    public void delete(Integer id) {
        Event event = find(id);

        eventRepository.delete(event);

        changePublisher.publish(EntityType.EVENT, ChangeType.DELETED, id);
    }

    private Event find(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event с id=" + id + " не найден"
                        )
                );
    }
}