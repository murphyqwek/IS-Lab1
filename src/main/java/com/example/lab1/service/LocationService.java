package com.example.lab1.service;

import com.example.lab1.dto.request.LocationRequest;
import com.example.lab1.entity.Location;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.LocationMapper;
import com.example.lab1.repository.LocationRepository;
import com.example.lab1.websocket.ChangeType;
import com.example.lab1.websocket.EntityChangePublisher;
import com.example.lab1.websocket.EntityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final EntityChangePublisher changePublisher;

    public LocationService(
            LocationRepository locationRepository,
            LocationMapper locationMapper,
            EntityChangePublisher changePublisher
    ) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.changePublisher = changePublisher;
    }

    @Transactional
    public Location resolve(LocationRequest request) {
        Location location = locationMapper.toEntity(request);
        return locationRepository.save(location);
    }

    @Transactional
    public Location update(long id, LocationRequest request) {
        Location location = find(id);

        location.setX(request.x());
        location.setY(request.y());
        location.setZ(request.z());
        location.setName(request.name());

        changePublisher.publish(EntityType.LOCATION, ChangeType.UPDATED, id);

        return location;
    }

    @Transactional
    public void delete(long id) {
        Location location = find(id);

        locationRepository.delete(location);

        changePublisher.publish(EntityType.LOCATION, ChangeType.DELETED, id);
    }

    private Location find(long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location с id=" + id + " не найдена"
                        )
                );
    }
}