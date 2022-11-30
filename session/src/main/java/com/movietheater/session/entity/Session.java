package com.movietheater.session.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "id_room")
    private UUID idRoom;

    @Column(name = "id_movie")
    private UUID idMovie;

    @Column(name = "date_time_session")
    private LocalDateTime dateTimeSession;

    @Column(name = "open_sale")
    private LocalDateTime openSale;

    @Column(name = "close_sale")
    private LocalDateTime closeSale;
}
