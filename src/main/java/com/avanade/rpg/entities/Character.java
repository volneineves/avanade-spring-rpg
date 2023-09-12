package com.avanade.rpg.entities;

import com.avanade.rpg.enums.CharacterType;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private CharacterType type;

    @Column(nullable = false)
    private short health;

    @Column(nullable = false)
    private short strength;

    @Column(nullable = false)
    private short defense;

    @Column(nullable = false)
    private short agility;

    @Column(nullable = false)
    private short numDice;

    @Column(nullable = false)
    private short faces;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public short getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }

    public short getStrength() {
        return strength;
    }

    public void setStrength(short strength) {
        this.strength = strength;
    }

    public short getDefense() {
        return defense;
    }

    public void setDefense(short defense) {
        this.defense = defense;
    }

    public short getAgility() {
        return agility;
    }

    public void setAgility(short agility) {
        this.agility = agility;
    }

    public short getNumDice() {
        return numDice;
    }

    public void setNumDice(short numDice) {
        this.numDice = numDice;
    }

    public short getFaces() {
        return faces;
    }

    public void setFaces(short faces) {
        this.faces = faces;
    }
}
