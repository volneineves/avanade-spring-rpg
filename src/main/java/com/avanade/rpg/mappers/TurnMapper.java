package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.payloads.responses.TurnResponse;
import org.springframework.stereotype.Component;

@Component
public class TurnMapper {

    public TurnResponse toResponse(Turn turn) {
        return new TurnResponse(
                turn.getId(),
                turn.getAttackValue(),
                turn.getDefenseValue(),
                turn.getDamage()
        );
    }
}
