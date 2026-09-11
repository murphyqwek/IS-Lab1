package com.example.lab1.service;

import com.example.lab1.dto.request.PersonReferenceRequest;
import com.example.lab1.dto.request.PersonRequest;
import com.example.lab1.dto.response.PersonResponse;
import com.example.lab1.entity.Location;
import com.example.lab1.entity.Person;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.PersonMapper;
import com.example.lab1.repository.PersonRepository;
import com.example.lab1.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final TicketRepository ticketRepository;
    private final LocationService locationService;
    private final PersonMapper mapper;

    public PersonService(PersonRepository repository, TicketRepository ticketRepository, LocationService locationService, PersonMapper mapper) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse getById(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public PersonResponse create(PersonRequest request) {
        Person person = createEntity(request);
        return mapper.toResponse(person);
    }

    @Transactional
    public PersonResponse update(Long id, PersonRequest request) {
        Person person = find(id);
        applyRequest(person, request);
        return mapper.toResponse(person);
    }

    @Transactional
    public void delete(Long id, Long replacementId) {
        Person person = find(id);
        var tickets = ticketRepository.findAllByPerson_Id(id);

        if (!tickets.isEmpty()) {
            ReferenceRequestValidator.requireReplacement(replacementId, "Person");
            ReferenceRequestValidator.requireDifferent(id, replacementId, "Person");

            Person replacement = find(replacementId);
            tickets.forEach(ticket -> ticket.setPerson(replacement));
        }

        repository.delete(person);
    }

    @Transactional
    public Person resolve(PersonReferenceRequest request) {
        ReferenceRequestValidator.requireExactlyOne(
                request.id(),
                request.newObject(),
                "person"
        );

        return request.id() != null
                ? find(request.id())
                : createEntity(request.newObject());
    }

    private Person createEntity(PersonRequest request) {
        Person person = new Person();
        applyRequest(person, request);
        return repository.save(person);
    }

    private void applyRequest(Person person, PersonRequest request) {
        Location location = request.location() == null
                ? null
                : locationService.resolve(request.location());

        person.setEyeColor(request.eyeColor());
        person.setHairColor(request.hairColor());
        person.setLocation(location);
        person.setWeight(request.weight());
        person.setNationality(request.nationality());
    }

    private Person find(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Person с id=" + id + " не найден"
                        )
                );
    }
}
