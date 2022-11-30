package com.movietheater.ticket.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.movietheater.ticket.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movietheater.ticket.entity.Ticket;
import com.movietheater.ticket.repository.TicketRepository;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketRepository repository;

    @Autowired
    private RestClient restClient;

    private static final String _SEAT_APPLICATION = "http://localhost:9002/seat";
    private static final String _SESSION_APPLICATION = "http://localhost:9003/session";


    @GetMapping
    public List<Ticket> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Optional<Ticket> ticket = this.repository.findById(id);
        if (ticket.isPresent()) {
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity createTicket(@RequestBody Ticket ticket) {
        ResponseEntity<Ticket> NOT_FOUND = verifyObjetsBeforeSave(ticket);
        if (NOT_FOUND != null) return NOT_FOUND;

        Optional<Ticket> ticketExist =this.repository.findByIdSessionAndIdSeat(ticket.getIdSession(),  ticket.getIdSeat());
        if (ticketExist.isPresent()) {
            return new ResponseEntity<>("Esse ticket já existe", HttpStatus.BAD_REQUEST);
        }
        Ticket save = this.repository.save(ticket);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Ticket ticket) {
        ResponseEntity<Ticket> NOT_FOUND = verifyObjetsBeforeSave(ticket);
        if (NOT_FOUND != null) return NOT_FOUND;

        if (!this.repository.existsById(ticket.getId()))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(this.repository.save(ticket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        this.repository.deleteById(id);
        Optional<Ticket> ticket = this.repository.findById(id);
        if (ticket.isPresent()) {
            return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Não encontrado ticket!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/session/{id}")
    public ResponseEntity getTicketBySession(@PathVariable UUID id) {
        return new ResponseEntity(this.repository.findAllByIdSession(id), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/session/{id}")
    public ResponseEntity deleteBySession(@PathVariable UUID id) {
        this.repository.deleteByIdSession(id);
        List<Ticket> ticketList = this.repository.findAllByIdSession(id);
        if (!ticketList.isEmpty()) {
            return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Não foi encontrado session!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/seat/{id}")
    public ResponseEntity getTicketBySeat(@PathVariable UUID id) {
        return new ResponseEntity<>(this.repository.findAllByIdSeat(id), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/seat/{id}")
    public ResponseEntity deleteByIdSeat(@PathVariable UUID id) {
        this.repository.deleteByIdSeat(id);
        List<Ticket> ticketList = this.repository.findAllByIdSeat(id);
        if(!ticketList.isEmpty()) {
            return new ResponseEntity("Deletado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Não foi encontrado seat!", HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Ticket> verifyObjetsBeforeSave(Ticket ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(headers);

        var seatExists = this.restClient.template(restTemplate ->
                restTemplate.exchange(_SEAT_APPLICATION+"/exists/"+ ticket.getIdSeat(), HttpMethod.GET, httpEntity, Boolean.class)
        ).getBody();
        if (!seatExists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        var sessionExists = this.restClient.template(restTemplate ->
                restTemplate.exchange(_SESSION_APPLICATION+"/exists/"+ ticket.getIdSession(), HttpMethod.GET, httpEntity, Boolean.class)
        ).getBody();
        if (!sessionExists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return null;
    }
}
