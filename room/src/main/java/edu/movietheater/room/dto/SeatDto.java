package edu.movietheater.room.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SeatDto {
    private UUID id;
    private String row;
    private String column;
}
