package edu.movietheater.seat.controller;

import edu.movietheater.seat.entity.Seat;
import edu.movietheater.seat.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        Seat save = this.repository.save(seat);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody Seat seat) {
        Seat save = this.repository.save(seat);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity getSeatsByRoom(@PathVariable UUID id) {
        Iterable<Seat> all = this.repository.findAllByRoom_Id(id);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/room/{id}")
    public ResponseEntity deleteSeatsByRoom(@PathVariable UUID id) {
        this.repository.deleteByRoom_Id(id);
        return new ResponseEntity<>("Deletados com sucesso!", HttpStatus.OK);
    }

}
