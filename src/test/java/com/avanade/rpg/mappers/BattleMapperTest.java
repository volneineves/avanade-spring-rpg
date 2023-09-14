package com.avanade.rpg.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.payloads.responses.TurnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

class BattleMapperTest {

    @Mock
    private TurnMapper turnMapper;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private BattleMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConvertToEntity() {
        Character initiative = new Character();
        initiative.setName("Initiative Character");

        Character opponent = new Character();
        opponent.setName("Opponent Character");

        Battle entity = mapper.toEntity(initiative, opponent);

        assertEquals(initiative, entity.getInitiative());
        assertEquals(opponent, entity.getOpponent());
    }
    @Test
    void shouldConvertToResponse() {
        UUID id = UUID.randomUUID();
        Turn turn = new Turn();
        turn.setStatus(TurnStatus.RUNNING);

        Character initiative = new Character();
        Character opponent = new Character();

        Battle battle = new Battle();
        battle.setInitiative(initiative);
        battle.setOpponent(opponent);
        battle.setId(id);

        TurnResponse turnResponse = new TurnResponse(UUID.randomUUID(), 1, 10, 5, 2, TurnStatus.STARTED);
        CharacterResponse initiativeResponse = new CharacterResponse(UUID.randomUUID(), "Lobisomen", (short) 100, (short) 10, (short) 5, (short) 2, (short) 1, (short) 6, CharacterType.MONSTER);
        CharacterResponse opponentResponse = new CharacterResponse(UUID.randomUUID(), "Guerreiro", (short) 90, (short) 8, (short) 4, (short) 3, (short) 1, (short) 4, CharacterType.HERO);

        when(turnMapper.toResponse(turn)).thenReturn(turnResponse);
        when(characterMapper.toResponse(initiative)).thenReturn(initiativeResponse);
        when(characterMapper.toResponse(opponent)).thenReturn(opponentResponse);

        BattleResponse response = mapper.toResponse(battle);

        assertEquals(id, response.id());
        assertEquals(initiativeResponse, response.initiative());
        assertEquals(opponentResponse, response.opponent());
        assertEquals(new ArrayList<>(), response.turns());
    }
}
