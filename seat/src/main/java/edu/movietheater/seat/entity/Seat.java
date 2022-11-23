package edu.movietheater.seat.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "num_column", length = 2)
    private String column;

    @Column(name = "num_row", length = 2)
    private String row;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private Room room;

}
