package com.movietheater.ticket.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "id_session")
    private UUID idSession;

    @Column(name = "id_seat")
    private UUID idSeat;

}
