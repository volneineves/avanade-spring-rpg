package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.payloads.responses.TurnResponse;
import org.springframework.stereotype.Component;

@Component
public class TurnMapper {

    public TurnResponse toResponse(Turn turn) {
        return new TurnResponse(
                turn.getId(),
                turn.getNumTurn(),
                turn.getAttack(),
                turn.getDefense(),
                turn.getDamage(),
                turn.getStatus()
        );
    }

    public Turn toEntity(Battle battle, TurnStatus status) {
        Turn turn = new Turn();
        turn.setNumTurn(battle.getNextTurnNumber());
        turn.setBattle(battle);
        turn.setStatus(status);
        return turn;
    }
}
