package com.example.lab1.mapper;

import com.example.lab1.dto.request.LocationRequest;
import com.example.lab1.dto.response.LocationResponse;
import com.example.lab1.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public Location toEntity(LocationRequest location) {
        if(location == null) {
            return null;
        }

        var entity = new Location();

        entity.setName(location.name());
        entity.setX(location.x());
        entity.setY(location.y());
        entity.setZ(location.z());

        return entity;
    }

    public LocationResponse toResponse(Location location) {
        if(location == null) {
            return null;
        }

        return new LocationResponse(location.getId(), location.getX(), location.getY(), location.getZ(), location.getName());
    }
}
