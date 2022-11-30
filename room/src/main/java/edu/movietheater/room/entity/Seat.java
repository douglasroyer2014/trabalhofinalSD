package edu.movietheater.room.entity;

import java.util.UUID;

import lombok.Data;

@Data
public class Seat {

    private UUID id;
    private String column;
    private String row;
    private UUID idRoom;
}
