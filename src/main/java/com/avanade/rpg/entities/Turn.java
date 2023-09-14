package com.avanade.rpg.entities;


import com.avanade.rpg.enums.TurnStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "turns")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer attack;
    private Integer defense;

    private Integer damage;

    @Column(nullable = false)
    private Integer numTurn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TurnStatus status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "attacker_id")
    private Character attacker;

    @JoinColumn(name = "defender_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Character defender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Battle battle;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attackValue) {
        this.attack = attackValue;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defenseValue) {
        this.defense = defenseValue;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getNumTurn() {
        return numTurn;
    }

    public void setNumTurn(Integer numTurn) {
        this.numTurn = numTurn;
    }

    public TurnStatus getStatus() {
        return status;
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public Character getAttacker() {
        return attacker;
    }

    public void setAttacker(Character attacker) {
        this.attacker = attacker;
    }

    public Character getDefender() {
        return defender;
    }

    public void setDefender(Character defender) {
        this.defender = defender;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}
