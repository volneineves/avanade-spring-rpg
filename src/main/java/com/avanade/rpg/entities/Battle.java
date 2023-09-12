package com.avanade.rpg.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "battles")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne
    private Character hero;

    @ManyToOne
    private Character monster;

    @OneToMany(mappedBy = "battle")
    private List<Turn> turns;
}
