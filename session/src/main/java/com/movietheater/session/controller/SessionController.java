package com.movietheater.session.controller;

import com.movietheater.session.RestClient;
import com.movietheater.session.entity.Session;
import com.movietheater.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private SessionRepository repository;

    @Autowired
    private RestClient restClient;

    private static final String _MOVIE_APPLICATION = "http://localhost:9000/movie";
    private static final String _ROOM_APPLICATION = "http://localhost:9001/room";
    private static final String _TICKET_APPLICATION = "http://localhost:9004/ticket";

    @GetMapping
    public List<Session> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Optional<Session> sessionOp = this.repository.findById(id);
        if (sessionOp.isPresent()) {
            return new ResponseEntity<>(sessionOp.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Session> create(@RequestBody Session session) {
        ResponseEntity<Session> NOT_FOUND = verifyObjetsBeforeSave(session);
        if (NOT_FOUND != null) return NOT_FOUND;

        Session save = this.repository.save(session);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity verifyIfExists(@PathVariable UUID id) {
        if (this.repository.existsById(id))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Session session) {
        ResponseEntity<Session> NOT_FOUND = verifyObjetsBeforeSave(session);
        if (NOT_FOUND != null) return NOT_FOUND;

        if (!this.repository.existsById(session.getId()))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Session save = this.repository.save(session);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity getSessionsByRoom(@PathVariable UUID id) {
        Iterable<Session> all = this.repository.findAllByIdRoom(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/room/{id}")
    public ResponseEntity deleteSessionsByRoom(@PathVariable UUID id) {
        this.repository.deleteByIdRoom(id);
        return new ResponseEntity<>("Deletados com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity getSessionsByMovie(@PathVariable UUID id) {
        Iterable<Session> all = this.repository.findAllByIdMovie(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/movie/{id}")
    public ResponseEntity deleteSessionsByMovie(@PathVariable UUID id) {
        this.repository.deleteByIdMovie(id);
        return new ResponseEntity<>("Deletados com sucesso!", HttpStatus.OK);
    }

    private ResponseEntity<Session> verifyObjetsBeforeSave(Session session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(headers);

        var roomExists = this.restClient.template(restTemplate ->
                restTemplate.exchange(_ROOM_APPLICATION+"/exists/"+ session.getIdRoom(), HttpMethod.GET, httpEntity, Boolean.class)
        ).getBody();
        if (!roomExists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        var movieExists = this.restClient.template(restTemplate ->
                restTemplate.exchange(_MOVIE_APPLICATION+"/exists/"+ session.getIdMovie(), HttpMethod.GET, httpEntity, Boolean.class)
        ).getBody();
        if (!movieExists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return null;
    }

}
