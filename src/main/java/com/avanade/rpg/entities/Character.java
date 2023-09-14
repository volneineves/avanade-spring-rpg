package com.avanade.rpg.entities;

import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.enums.DiceFaces;
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
    private Short health;

    @Column(nullable = false)
    private Short strength;

    @Column(nullable = false)
    private Short defense;

    @Column(nullable = false)
    private Short agility;

    @Column(nullable = false)
    private Short numDice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiceFaces faces;

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

    public Short getHealth() {
        return health;
    }

    public void setHealth(Short health) {
        this.health = health;
    }

    public Short getStrength() {
        return strength;
    }

    public void setStrength(Short strength) {
        this.strength = strength;
    }

    public Short getDefense() {
        return defense;
    }

    public void setDefense(Short defense) {
        this.defense = defense;
    }

    public Short getAgility() {
        return agility;
    }

    public void setAgility(Short agility) {
        this.agility = agility;
    }

    public Short getNumDice() {
        return numDice;
    }

    public void setNumDice(Short numDice) {
        this.numDice = numDice;
    }

    public DiceFaces getFaces() {
        return faces;
    }

    public void setFaces(DiceFaces faces) {
        this.faces = faces;
    }
}
