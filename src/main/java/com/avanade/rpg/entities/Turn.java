package com.avanade.rpg.entities;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "turns")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private short attackValue;
    private short defenseValue;
    private short damage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public short getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(short attackValue) {
        this.attackValue = attackValue;
    }

    public short getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(short defenseValue) {
        this.defenseValue = defenseValue;
    }

    public short getDamage() {
        return damage;
    }

    public void setDamage(short damage) {
        this.damage = damage;
    }
}
