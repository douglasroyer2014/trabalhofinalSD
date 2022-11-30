package edu.movietheater.room.repository;

import edu.movietheater.room.entity.Room;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends CrudRepository<Room, UUID> {
    List<Room> findAll();

    @Transactional
    @Modifying
    @Query("update Room r set r.capacity = ?1 where r.id = ?2")
    int updateCapacityById(Integer capacity, UUID id);

}
