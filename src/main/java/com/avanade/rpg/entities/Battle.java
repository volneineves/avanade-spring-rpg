package com.avanade.rpg.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "battles")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "initiative_id")
    private Character initiative;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "opponent_id")
    private Character opponent;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "battle_id")
    private List<Turn> turns = new ArrayList<>();

    private String winner;

    public UUID getId() {
        return id;
    }

    public Character getInitiative() {
        return initiative;
    }

    public void setInitiative(Character initiative) {
        this.initiative = initiative;
    }

    public Character getOpponent() {
        return opponent;
    }

    public void setOpponent(Character opponent) {
        this.opponent = opponent;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void addTurn(Turn turn) {
        this.turns.add(turn);
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
