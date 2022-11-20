package com.movie.movie.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.movie.movie.entity.Movie;
import com.movie.movie.repository.MovieRepository;

@RestController
@RequestMapping("/movie")
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Transactional
    @GetMapping()
    public @ResponseBody List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    @PostMapping()
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieRepository.save(movie), HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity getMovie(@PathVariable UUID id) {
        return new ResponseEntity<>(movieRepository.findById(id), HttpStatus.OK);
    }

    @Transactional
    @PutMapping
    public ResponseEntity putMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieRepository.save(movie), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable UUID id) {
        movieRepository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }
}
