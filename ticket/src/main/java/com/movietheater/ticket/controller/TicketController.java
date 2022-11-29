package com.movietheater.ticket.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.movietheater.ticket.entity.Ticket;
import com.movietheater.ticket.message.MessagePublisher;
import com.movietheater.ticket.repository.TicketRepository;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketRepository repository;

    @Autowired
    MessagePublisher messagePublisher;

    @GetMapping
    public List<Ticket> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Optional<Ticket> ticket = this.repository.findById(id);
        if (ticket.isEmpty()) {
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket save = this.repository.save(ticket);
        messagePublisher.publishMessage(save);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(this.repository.save(ticket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/session/{id}")
    public ResponseEntity getTicketBySession(@PathVariable UUID id) {
        return new ResponseEntity(this.repository.findAllByIdSession(id), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/session/{id}")
    public ResponseEntity deleteBySession(@PathVariable UUID id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/seat/{id}")
    public ResponseEntity getTicketBySeat(@PathVariable UUID id) {
        return new ResponseEntity<>(this.repository.findAllByIdSeat(id), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/seat/{id}")
    public ResponseEntity deleteByIdSeat(@PathVariable UUID id) {
        this.repository.deleteByIdSeat(id);
        return new ResponseEntity("Deletado com sucesso!", HttpStatus.OK);
    }
}
