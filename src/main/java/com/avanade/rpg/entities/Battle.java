package com.avanade.rpg.entities;

import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "battles", schema = "game")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "initiative_id")
    private Character initiative;

    @ManyToOne(optional = false)
    @JoinColumn(name = "opponent_id")
    private Character opponent;

    @OneToMany(mappedBy = "battle")
    @JsonManagedReference
    private List<Turn> turns = new ArrayList<>();

    private String winner;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        turns.add(turn);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getNextTurnNumber() {
        return this.turns.size() + 1;
    }

    public void validateCanAddNewTurn() {
        boolean hasTurnsWhoDoesNotFinishedYet =  this.turns.stream().anyMatch(turn -> !TurnStatus.FINISHED.equals(turn.getStatus()));
        if (hasTurnsWhoDoesNotFinishedYet) {
            throw new BadRequestException("Battle has already turn running");
        }
    }
}
