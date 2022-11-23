package edu.movietheater.room.repository;

import edu.movietheater.room.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends CrudRepository<Room, UUID> {
    List<Room> findAll();
}
