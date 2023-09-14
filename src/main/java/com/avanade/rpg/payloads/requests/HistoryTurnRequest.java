package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class HistoryTurnRequest implements Serializable {

    private final UUID battleId;
    private final Integer attack;
    private final Integer defense;
    private final Integer damage;
    private final Integer numTurn;
    private final HistoryCharacter attacker;
    private final HistoryCharacter defender;
    private final Date updatedAt = new Date();

    public HistoryTurnRequest(Turn turn) {
        this.battleId = turn.getBattle().getId();
        this.attack = turn.getAttack();
        this.defense = turn.getDefense();
        this.damage = turn.getDamage();
        this.numTurn = turn.getNumTurn();
        this.attacker = turn.getAttacker() != null ? new HistoryCharacter(turn.getAttacker()) : null;
        this.defender = turn.getDefender() != null ? new HistoryCharacter(turn.getDefender()) : null;
    }

    public UUID getBattleId() {
        return battleId;
    }

    public Integer getAttack() {
        return attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getDamage() {
        return damage;
    }

    public Integer getNumTurn() {
        return numTurn;
    }

    public HistoryCharacter getAttacker() {
        return attacker;
    }

    public HistoryCharacter getDefender() {
        return defender;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    private static class HistoryCharacter implements Serializable {
        private final String name;
        private final short health;
        private final short strength;
        private final short defense;
        private final short agility;
        private final short numDice;
        private final DiceFaces faces;

        public HistoryCharacter(Character character) {
            this.name = character.getName();
            this.health = character.getHealth();
            this.strength = character.getStrength();
            this.defense = character.getDefense();
            this.agility = character.getAgility();
            this.numDice = character.getNumDice();
            this.faces = character.getFaces();
        }

        public String getName() {
            return name;
        }

        public short getHealth() {
            return health;
        }

        public short getStrength() {
            return strength;
        }

        public short getDefense() {
            return defense;
        }

        public short getAgility() {
            return agility;
        }

        public short getNumDice() {
            return numDice;
        }

        public DiceFaces getFaces() {
            return faces;
        }
    }

}