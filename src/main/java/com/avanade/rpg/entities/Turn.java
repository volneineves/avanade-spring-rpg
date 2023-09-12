package com.avanade.rpg.entities;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "turns")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Battle battle;

    private short attackValue;
    private short defenseValue;
    private short damage;
}
