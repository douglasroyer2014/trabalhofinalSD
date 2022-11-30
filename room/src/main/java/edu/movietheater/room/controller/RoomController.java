package edu.movietheater.room.controller;

import edu.movietheater.room.RestClient;
import edu.movietheater.room.dto.RoomDto;
import edu.movietheater.room.dto.SeatDto;
import edu.movietheater.room.entity.Room;
import edu.movietheater.room.message.MessagePublisher;
import edu.movietheater.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository repository;

    @Autowired
    private MessagePublisher messagePublisher;

    private static final String _SEAT_APPLICATION = "http://localhost:9002/seat";

    @Autowired
    private RestClient restClient;

    @GetMapping
    public List<Room> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity verifyIfExists(@PathVariable UUID id) {
        if (this.repository.existsById(id))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
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
                    restTemplate.exchange(_SEAT_APPLICATION+"/room/"+room.getId(), HttpMethod.GET, httpEntity, Object.class)
            ).getBody();
            RoomDto roomDto = RoomDto.builder()
                    .id(room.getId())
                    .name(room.getName())
                    .seats(seats)
                    .build();
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        room.setCapacity(0);
        return new ResponseEntity<>(this.repository.save(room), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody Room room) {
        if (!this.repository.existsById(room.getId()))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(this.repository.save(room), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        Optional<Room> room = this.repository.findById(id);
        if (room.isPresent()) {
            this.messagePublisher.publishMessage(room.get(), "removeRoom");
            this.repository.deleteById(id);
            return new ResponseEntity<>("Deletado com sucesso!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Não foi encontrado", HttpStatus.NOT_FOUND);
    }

}
