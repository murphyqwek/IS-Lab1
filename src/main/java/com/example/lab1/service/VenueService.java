package com.example.lab1.service;

import com.example.lab1.dto.request.VenueRequest;
import com.example.lab1.entity.Venue;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.VenueMapper;
import com.example.lab1.repository.VenueRepository;
import com.example.lab1.websocket.ChangeType;
import com.example.lab1.websocket.EntityChangePublisher;
import com.example.lab1.websocket.EntityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final EntityChangePublisher changePublisher;

    public VenueService(
            VenueRepository venueRepository,
            VenueMapper venueMapper,
            EntityChangePublisher changePublisher
    ) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
        this.changePublisher = changePublisher;
    }

    @Transactional
    public Venue resolve(VenueRequest request) {
        Venue venue = venueMapper.toEntity(request);
        return venueRepository.save(venue);
    }

    @Transactional
    public Venue update(Integer id, VenueRequest request) {
        Venue venue = find(id);

        venue.setName(request.name());
        venue.setCapacity(request.capacity());
        venue.setType(request.venueType());

        changePublisher.publish(EntityType.VENUE, ChangeType.UPDATED, id);

        return venue;
    }

    @Transactional
    public void delete(Integer id) {
        Venue venue = find(id);

        venueRepository.delete(venue);

        changePublisher.publish(EntityType.VENUE, ChangeType.DELETED, id);
    }

    private Venue find(Integer id) {
        return venueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue с id=" + id + " не найден"
                        )
                );
    }
}