package com.example.lab1.mapper;

import com.example.lab1.dto.request.EventRequest;
import com.example.lab1.dto.response.EventResponse;
import com.example.lab1.dto.response.VenueResponse;
import com.example.lab1.entity.Event;
import com.example.lab1.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toEntity(EventRequest request) {
        var event = new Event();

        event.setEventType(request.eventType());
        event.setName(request.name());
        event.setDescription(request.description());

        return event;
    }

    public EventResponse toResponse(Event event) {
        return new EventResponse(event.getId(), event.getName(), event.getDescription(), event.getEventType());
    }
}
