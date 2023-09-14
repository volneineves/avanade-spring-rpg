package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.entities.Battle;

import java.io.Serializable;
import java.util.UUID;

public class HistoryBattleRequest implements Serializable {

    private final UUID battleId;
    private final String initiativeName;
    private final String opponentName;
    private final String winner;


    public HistoryBattleRequest(Battle battle) {
        this.battleId = battle.getId();
        this.initiativeName = battle.getInitiative() != null ? battle.getInitiative().getName() : null;
        this.opponentName = battle.getOpponent() != null ? battle.getOpponent().getName() : null;
        this.winner = battle.getWinner();
    }

    public UUID getBattleId() {
        return battleId;
    }

    public String getInitiativeName() {
        return initiativeName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getWinner() {
        return winner;
    }
}
