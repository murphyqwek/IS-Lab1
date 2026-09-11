package com.example.lab1.service;

import com.example.lab1.dto.request.LocationReferenceRequest;
import com.example.lab1.dto.request.LocationRequest;
import com.example.lab1.dto.response.LocationResponse;
import com.example.lab1.entity.Location;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.LocationMapper;
import com.example.lab1.repository.LocationRepository;
import com.example.lab1.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository repository;
    private final PersonRepository personRepository;
    private final LocationMapper mapper;

    public LocationService(
            LocationRepository repository,
            PersonRepository personRepository,
            LocationMapper mapper
    ) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getById(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public LocationResponse create(LocationRequest request) {
        Location location = createEntity(request);
        return mapper.toResponse(location);
    }

    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location location = find(id);

        location.setX(request.x());
        location.setY(request.y());
        location.setZ(request.z());
        location.setName(request.name());

        return mapper.toResponse(location);
    }

    @Transactional
    public void delete(Long id, Long replacementId) {
        Location location = find(id);
        var persons = personRepository.findAllByLocation_Id(id);

        if (!persons.isEmpty()) {
            ReferenceRequestValidator.requireReplacement(replacementId, "Location");
            ReferenceRequestValidator.requireDifferent(id, replacementId, "Location");

            Location replacement = find(replacementId);
            persons.forEach(person -> person.setLocation(replacement));
        }

        repository.delete(location);
    }

    @Transactional
    public Location resolve(LocationReferenceRequest request) {
        ReferenceRequestValidator.requireExactlyOne(
                request.id(),
                request.newObject(),
                "location"
        );

        return request.id() != null
                ? find(request.id())
                : createEntity(request.newObject());
    }

    private Location createEntity(LocationRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    private Location find(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location с id=" + id + " не найден"
                        )
                );
    }
}
