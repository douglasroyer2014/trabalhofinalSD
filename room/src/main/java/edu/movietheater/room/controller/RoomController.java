package edu.movietheater.room.controller;

import edu.movietheater.room.RestClient;
import edu.movietheater.room.dto.RoomDto;
import edu.movietheater.room.dto.SeatDto;
import edu.movietheater.room.entity.Room;
import edu.movietheater.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository repository;

    @Autowired
    private RestClient restClient;

    @GetMapping
    public List<Room> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Optional<Room> roomOp = this.repository.findById(id);
        if (roomOp.isPresent()) {
            Room room = roomOp.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            var httpEntity = new HttpEntity<>(headers);
            var seats = (List<SeatDto>) this.restClient.template(restTemplate ->
                    restTemplate.exchange("http://localhost:9002/seat/room/"+room.getId(), HttpMethod.GET, httpEntity, Object.class)
            ).getBody();
            RoomDto roomDto = RoomDto.builder()
                    .id(room.getId())
                    .name(room.getName())
                    .seats(seats)
                    .build();
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(this.repository.save(room), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody Room room) {
        return new ResponseEntity<>(this.repository.save(room), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);
        this.restClient.template(restTemplate ->
                restTemplate.exchange("http://localhost:9002/seat/room/"+id, HttpMethod.DELETE, httpEntity, Void.class)
        );

        this.repository.deleteById(id);
        return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
    }

}
