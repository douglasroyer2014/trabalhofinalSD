package com.movietheater.session.repository;

import com.movietheater.session.entity.Session;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {
    List<Session> findAll();
    List<Session> findAllByIdRoom(UUID id);
    void deleteByIdRoom(UUID id);
    List<Session> findAllByIdMovie(UUID id);
    void deleteByIdMovie(UUID id);

    @Transactional
    @Modifying
    @Query("update Session s set s.qntTicket = ?1 where s.id = ?2")
    int updateQntTicketById(Long qntTicket, UUID id);

}
