package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.payloads.responses.TurnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TurnMapperTest {

    private TurnMapper turnMapper;

    @BeforeEach
    void setUp() {
        turnMapper = new TurnMapper();
    }

    @Test
    void shouldConvertToResponse() {
        UUID id = UUID.randomUUID();
        Turn turn = new Turn();
        turn.setId(id);
        turn.setNumTurn(1);
        turn.setAttack(10);
        turn.setDefense(5);
        turn.setDamage(2);
        turn.setStatus(TurnStatus.RUNNING);

        TurnResponse response = turnMapper.toResponse(turn);

        assertEquals(id, response.id());
        assertEquals(1, response.numTurn());
        assertEquals(10, response.attack());
        assertEquals(5, response.defense());
        assertEquals(2, response.damage());
        assertEquals(TurnStatus.RUNNING, response.status());
    }

    @Test
    void shouldConvertToEntity() {
        Battle battle = mock(Battle.class);
        when(battle.getNextTurnNumber()).thenReturn(2);

        TurnStatus status = TurnStatus.STARTED;
        Turn turn = turnMapper.toEntity(battle, status);

        assertEquals(2, turn.getNumTurn());
        assertEquals(battle, turn.getBattle());
        assertEquals(status, turn.getStatus());
    }
}
