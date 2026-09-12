package com.example.lab1.service;

import com.example.lab1.dto.request.CoordinatesRequest;
import com.example.lab1.entity.Coordinates;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.CoordinatesMapper;
import com.example.lab1.repository.CoordinatesRepository;
import com.example.lab1.websocket.ChangeType;
import com.example.lab1.websocket.EntityChangePublisher;
import com.example.lab1.websocket.EntityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final CoordinatesMapper coordinatesMapper;
    private final EntityChangePublisher changePublisher;

    public CoordinatesService(
            CoordinatesRepository coordinatesRepository,
            CoordinatesMapper coordinatesMapper,
            EntityChangePublisher changePublisher
    ) {
        this.coordinatesRepository = coordinatesRepository;
        this.coordinatesMapper = coordinatesMapper;
        this.changePublisher = changePublisher;
    }

    @Transactional
    public Coordinates resolve(CoordinatesRequest request) {
        Coordinates coordinates = coordinatesMapper.toEntity(request);
        return coordinatesRepository.save(coordinates);
    }

    @Transactional
    public Coordinates update(long id, CoordinatesRequest request) {
        Coordinates coordinates = find(id);

        coordinates.setX(request.x());
        coordinates.setY(request.y());

        changePublisher.publish(EntityType.COORDINATES, ChangeType.UPDATED, id);

        return coordinates;
    }

    @Transactional
    public void delete(long id) {
        Coordinates coordinates = find(id);

        coordinatesRepository.delete(coordinates);

        changePublisher.publish(EntityType.COORDINATES,ChangeType.DELETED, id);
    }

    private Coordinates find(long id) {
        return coordinatesRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coordinates с id=" + id + " не найдены"
                        )
                );
    }
}