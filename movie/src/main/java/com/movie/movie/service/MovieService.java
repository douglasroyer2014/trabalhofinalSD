package com.movie.movie.service;

import java.util.List;
import java.util.Optional;
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
import com.movie.movie.message.MessagePublisher;
import com.movie.movie.repository.MovieRepository;

@RestController
@RequestMapping("/movie")
public class MovieService {

    @Autowired
    MovieRepository repository;

    @Autowired
    MessagePublisher messagePublisher;

    @Transactional
    @GetMapping()
    public @ResponseBody List<Movie> getMovies() {
        return repository.findAll();
    }

    @Transactional
    @PostMapping()
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(repository.save(movie), HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity getMovie(@PathVariable UUID id) {
        return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity verifyIfExists(@PathVariable UUID id) {
        if (this.repository.existsById(id))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PutMapping
    public ResponseEntity update(@RequestBody Movie movie) {
        if (!this.repository.existsById(movie.getId()))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(repository.save(movie), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable UUID id) {
        Optional<Movie> movie = this.repository.findById(id);
        if (movie.isPresent()) {
            repository.deleteById(id);
            this.messagePublisher.publishMessage(movie.get(), "removeMovie");
            return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Não foi encontrado o filme", HttpStatus.NOT_FOUND);
    }
}
