package com.example.lab1.repository;

import com.example.lab1.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TicketRepository
        extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findAllByEvent_Id(Integer eventId);

    List<Ticket> findAllByVenue_Id(Integer venueId);

    List<Ticket> findAllByPerson_Id(Long personId);

    List<Ticket> findAllByCoordinates_Id(Long coordinatesId);
}
