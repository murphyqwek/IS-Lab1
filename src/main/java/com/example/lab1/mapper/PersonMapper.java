package com.example.lab1.mapper;

import com.example.lab1.dto.request.PersonRequest;
import com.example.lab1.dto.response.PersonResponse;
import com.example.lab1.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    private final LocationMapper locationMapper;

    public PersonMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public Person toEntity(PersonRequest request) {
        if (request == null) {
            return null;
        }

        Person person = new Person();

        person.setEyeColor(request.eyeColor());
        person.setHairColor(request.hairColor());
        person.setLocation(locationMapper.toEntity(request.locationRequest()));
        person.setNationality(request.nationality());
        person.setWeight(request.weight());

        return person;
    }

    public PersonResponse toResponse(Person person) {
        if (person == null) {
            return null;
        }

        return new PersonResponse(
                person.getId(),
                person.getEyeColor(),
                person.getHairColor(),
                locationMapper.toResponse(person.getLocation()),
                person.getWeight(),
                person.getNationality()
        );
    }
}
