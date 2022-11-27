package com.movietheater.session.repository;

import com.movietheater.session.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {
    List<Session> findAll();
    List<Session> findAllByIdRoom(UUID id);
    void deleteByIdRoom(UUID id);
    List<Session> findAllByIdMovie(UUID id);
    void deleteByIdMovie(UUID id);
}
