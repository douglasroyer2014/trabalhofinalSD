package com.movietheater.ticket.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.movietheater.ticket.entity.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, UUID> {

    List<Ticket> findAll();
    List<Ticket> findAllByIdSession(UUID id);
    void deleteByIdSession(UUID id);
    List<Ticket> findAllByIdSeat(UUID id);
    void deleteByIdSeat(UUID id);
    Optional<Ticket> findByIdSessionAndIdSeat(UUID idSession, UUID idSeat);
}
