package edu.movietheater.seat.repository;

import edu.movietheater.seat.entity.Seat;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
public interface SeatRepository extends CrudRepository<Seat, UUID> {
    List<Seat> findAll();

    List<Seat> findAllByRoom_Id(UUID id);

    void deleteByRoom_Id(UUID id);

}
