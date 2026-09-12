package com.example.lab1.service;

import com.example.lab1.dto.request.PersonRequest;
import com.example.lab1.entity.Person;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.repository.PersonRepository;
import com.example.lab1.websocket.ChangeType;
import com.example.lab1.websocket.EntityChangePublisher;
import com.example.lab1.websocket.EntityType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LocationService locationService;
    private final EntityChangePublisher changePublisher;

    public PersonService(
            PersonRepository personRepository,
            LocationService locationService,
            EntityChangePublisher changePublisher
    ) {
        this.personRepository = personRepository;
        this.locationService = locationService;
        this.changePublisher = changePublisher;
    }

    @Transactional
    public Person resolve(PersonRequest request) {
        Person person = new Person();

        applyRequest(person, request);

        return personRepository.save(person);
    }

    @Transactional
    public Person update(long id, PersonRequest request) {
        Person person = find(id);

        applyRequest(person, request);

        changePublisher.publish(EntityType.PERSON, ChangeType.UPDATED, id);

        return person;
    }

    @Transactional
    public void delete(long id) {
        Person person = find(id);

        personRepository.delete(person);

        changePublisher.publish(EntityType.PERSON, ChangeType.DELETED, id);
    }

    private void applyRequest(Person person, PersonRequest request) {
        person.setEyeColor(request.eyeColor());
        person.setHairColor(request.hairColor());

        person.setLocation(request.location() == null ? null : locationService.resolve(request.location().newObject()));

        person.setWeight(request.weight());
        person.setNationality(request.nationality());
    }

    private Person find(long id) {
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Person с id=" + id + " не найден"
                        )
                );
    }
}