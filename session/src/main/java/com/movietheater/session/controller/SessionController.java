package com.movietheater.session.controller;

import com.movietheater.session.entity.Session;
import com.movietheater.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        session.setQntTicket(0L);
        Session save = this.repository.save(session);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody Session session) {
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

}
