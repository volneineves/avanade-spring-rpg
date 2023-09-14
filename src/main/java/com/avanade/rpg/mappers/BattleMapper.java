package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.payloads.responses.TurnResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleMapper {

    private final TurnMapper turnMapper;
    private final CharacterMapper characterMapper;

    public BattleMapper(TurnMapper turnMapper, CharacterMapper characterMapper) {
        this.turnMapper = turnMapper;
        this.characterMapper = characterMapper;
    }

    public Battle toEntity(Character initiative, Character opponent) {
        Battle battle = new Battle();
        battle.setInitiative(initiative);
        battle.setOpponent(opponent);
        return battle;
    }

    public BattleResponse toResponse(Battle battle) {
        List<TurnResponse> turnResponses = battle.getTurns()
                .stream()
                .map(turnMapper::toResponse)
                .toList();

        CharacterResponse initiative = characterMapper.toResponse(battle.getInitiative());
        CharacterResponse opponent = characterMapper.toResponse(battle.getOpponent());

        return new BattleResponse(
                battle.getId(),
                initiative,
                opponent,
                turnResponses,
                battle.getWinner()
        );
    }
}
