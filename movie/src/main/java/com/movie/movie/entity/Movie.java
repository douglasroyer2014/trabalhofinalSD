package com.movie.movie.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "classification")
    private Short classification;

}
