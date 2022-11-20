package com.movie.movie.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.movie.movie.entity.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {

    @Override
    List<Movie> findAll();
}
