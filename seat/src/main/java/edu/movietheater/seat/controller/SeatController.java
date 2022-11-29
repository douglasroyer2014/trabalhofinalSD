package edu.movietheater.seat.controller;

import edu.movietheater.seat.RestClient;
import edu.movietheater.seat.entity.Seat;
import edu.movietheater.seat.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @Autowired
    private SeatRepository repository;

    @Autowired
    private RestClient restClient;

    private static final String _ROOM_APPLICATION = "http://localhost:9001/room";

    @GetMapping
    public List<Seat> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Optional<Seat> seatOp = this.repository.findById(id);
        if (seatOp.isPresent()) {
            return new ResponseEntity<>(seatOp.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        ResponseEntity<Seat> NOT_FOUND = verifyObjetsBeforeSave(seat);
        if (NOT_FOUND != null) return NOT_FOUND;

        Seat save = this.repository.save(seat);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Seat seat) {
        ResponseEntity<Seat> NOT_FOUND = verifyObjetsBeforeSave(seat);
        if (NOT_FOUND != null) return NOT_FOUND;

        if (!this.repository.existsById(seat.getId()))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Seat save = this.repository.save(seat);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity verifyIfExists(@PathVariable UUID id) {
        if (this.repository.existsById(id))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity getSeatsByRoom(@PathVariable UUID id) {
        Iterable<Seat> all = this.repository.findAllByIdRoom(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/room/{id}")
    public ResponseEntity deleteSeatsByRoom(@PathVariable UUID id) {
        this.repository.deleteByIdRoom(id);
        return new ResponseEntity<>("Deletados com sucesso!", HttpStatus.OK);
    }

    private ResponseEntity<Seat> verifyObjetsBeforeSave(Seat seat) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(headers);

        var roomExists = this.restClient.template(restTemplate ->
                restTemplate.exchange(_ROOM_APPLICATION+"/exists/"+ seat.getIdRoom(), HttpMethod.GET, httpEntity, Boolean.class)
        ).getBody();
        if (!roomExists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return null;
    }

}
