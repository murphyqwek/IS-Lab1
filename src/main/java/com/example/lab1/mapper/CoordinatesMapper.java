package com.example.lab1.mapper;

import com.example.lab1.dto.request.CoordinatesRequest;
import com.example.lab1.dto.response.CoordinatesResponse;
import com.example.lab1.entity.Coordinates;
import org.springframework.stereotype.Component;

@Component
public class CoordinatesMapper {
    public Coordinates toEntity(CoordinatesRequest coordinates) {
        var coordinatesEntity = new Coordinates();

        coordinatesEntity.setX(coordinates.x());
        coordinatesEntity.setY(coordinates.y());

        return coordinatesEntity;
    }

    public CoordinatesResponse toResponse(Coordinates coordinates) {
        return new CoordinatesResponse(coordinates.getId(), coordinates.getX(),  coordinates.getY());
    }
}
