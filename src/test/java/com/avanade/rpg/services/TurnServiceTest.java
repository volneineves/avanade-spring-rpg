package com.avanade.rpg.services;

import com.avanade.rpg.amqp.HistoryPublisher;
import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.ActionType;
import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceNotFoundException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.factories.Action;
import com.avanade.rpg.factories.ActionFactory;
import com.avanade.rpg.mappers.TurnMapper;
import com.avanade.rpg.payloads.requests.ActionTurnRequest;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.repositories.TurnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnServiceTest {

    @Mock
    private TurnRepository repository;

    @Mock
    private TurnMapper mapper;

    @Mock
    private BattleService battleService;

    @Mock
    private ActionFactory actionFactory;

    @Mock
    private HistoryPublisher publisher;

    @InjectMocks
    private TurnService service;

    private UUID turnId;
    private UUID monsterId;
    private UUID battleId;
    private ActionTurnRequest actionTurnRequest;
    private Turn turn;
    private Action action;
    private Battle battle;
    private Character hero;
    private Character monster;
    private TurnResponse turnResponse;

    @BeforeEach
    void setUp() {
        turnId = UUID.randomUUID();
        monsterId = UUID.randomUUID();
        battleId = UUID.randomUUID();

        actionTurnRequest = new ActionTurnRequest(turnId, monsterId, ActionType.ATTACK);

        hero = mock(Character.class);
        monster = mock(Character.class);

        battle = mock(Battle.class);
        turn = mock(Turn.class);

        turnResponse = mock(TurnResponse.class);

        action = mock(Action.class);
    }


    @Test
    @DisplayName("Should create a new turn")
    void shouldCreateNewTurn() {
        when(mapper.toEntity(battle, TurnStatus.STARTED)).thenReturn(turn);

        Turn turn = service.createByBattle(battle);

        assertNotNull(turn);
        verify(publisher).processHistoryTurn(this.turn);
    }

    @Test
    @DisplayName("Should update turn by action Initiative")
    void shouldUpdateTurnByActionInitiative() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getBattle()).thenReturn(battle);
        when(turn.getBattle().getInitiative()).thenReturn(monster);
        when(turn.getBattle().getInitiative().getId()).thenReturn(monsterId);
        when(turn.getStatus()).thenReturn(TurnStatus.STARTED);

        when(actionFactory.createAction(ActionType.ATTACK)).thenReturn(action);
        when(mapper.toResponse(turn)).thenReturn(turnResponse);
        when(turnResponse.status()).thenReturn(TurnStatus.RUNNING);

        TurnResponse updatedTurn = service.updateByAction(actionTurnRequest);

        assertNotNull(updatedTurn);
        assertEquals(TurnStatus.RUNNING, turnResponse.status());
    }

    @Test
    @DisplayName("Should update turn by action Opponent")
    void shouldUpdateTurnByActionOpponent() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getBattle()).thenReturn(battle);
        when(turn.getBattle().getInitiative()).thenReturn(hero);
        when(turn.getBattle().getOpponent()).thenReturn(monster);
        when(turn.getBattle().getOpponent().getId()).thenReturn(monsterId);
        when(turn.getDefender()).thenReturn(hero);
        when(turn.getStatus()).thenReturn(TurnStatus.RUNNING);

        when(actionFactory.createAction(ActionType.ATTACK)).thenReturn(action);
        when(mapper.toResponse(turn)).thenReturn(turnResponse);
        when(turnResponse.status()).thenReturn(TurnStatus.RUNNING);

        TurnResponse updatedTurn = service.updateByAction(actionTurnRequest);

        assertNotNull(updatedTurn);
        assertEquals(TurnStatus.RUNNING, turnResponse.status());
    }

    @Test
    @DisplayName("Should throw BadRequestException when try to update with Iin")
    void shouldThrowBadRequestExceptionWhenTryToUpdateWithInvalidCharacter() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getStatus()).thenReturn(TurnStatus.RUNNING);
        when(turn.getBattle()).thenReturn(battle);
        when(turn.getBattle().getInitiative()).thenReturn(hero);
        when(turn.getBattle().getOpponent()).thenReturn(monster);

        assertThrows(BadRequestException.class, () -> service.updateByAction(actionTurnRequest));
    }

    @Test
    @DisplayName("Should calculate damage")
    void shouldCalculateDamage() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));

        when(repository.save(turn)).thenReturn(turn);
        when(mapper.toResponse(turn)).thenReturn(turnResponse);
        when(turnResponse.status()).thenReturn(TurnStatus.FINISHED);

        TurnResponse response = service.calculateDamage(turnId);

        verify(turn).setDamage(anyInt());
        assertEquals(TurnStatus.FINISHED, response.status());
    }

    @Test
    @DisplayName("Should calculate damage and update Battle winner")
    void shouldCalculateDamageAndUpdateBattleWinner() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getBattle()).thenReturn(battle);
        when(turn.getAttacker()).thenReturn(hero);
        when(turn.getDefender()).thenReturn(monster);
        when(turn.getAttack()).thenReturn(1000);

        when(repository.save(turn)).thenReturn(turn);
        when(mapper.toResponse(turn)).thenReturn(turnResponse);
        when(turnResponse.status()).thenReturn(TurnStatus.FINISHED);

        TurnResponse response = service.calculateDamage(turnId);

        verify(turn).setDamage(anyInt());
        assertEquals(TurnStatus.FINISHED, response.status());
        verify(publisher).processHistoryBattle(battle);
    }

    @Test
    @DisplayName("Should throw BadRequestException if turn already finished")
    void shouldThrowBadRequestExceptionIfTurnAlreadyFinished() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getStatus()).thenReturn(TurnStatus.FINISHED);

        assertThrows(BadRequestException.class, () -> service.updateByAction(actionTurnRequest));
    }

    @Test
    @DisplayName("Should throw BadRequestException if character without initiative starts the turn")
    void shouldThrowBadRequestExceptionForInvalidInitiative() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(turn.getStatus()).thenReturn(TurnStatus.STARTED);
        when(turn.getBattle()).thenReturn(battle);
        when(turn.getBattle().getOpponent()).thenReturn(monster);
        when(battle.getInitiative()).thenReturn(hero);

        assertThrows(BadRequestException.class, () -> service.updateByAction(actionTurnRequest));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if turn not exists")
    void shouldThrowResourceNotFoundException() {
        when(repository.findById(turnId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateByAction(actionTurnRequest));
        assertThrows(ResourceNotFoundException.class, () -> service.calculateDamage(turnId));
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when DataIntegrityViolationException is thrown")
    void shouldThrowConstraintViolationException() {
        when(mapper.toEntity(battle, TurnStatus.STARTED)).thenReturn(turn);

        doThrow(DataIntegrityViolationException.class).when(repository).save(turn);

        assertThrows(ConstraintViolationException.class, () -> service.createByBattle(battle));
    }

    @Test
    @DisplayName("Should throw UnknownViolationException for unknown exceptions")
    void shouldThrowUnknownViolationException() {
        when(repository.findById(turnId)).thenReturn(Optional.of(turn));
        when(battleService.findBattleByIdOrThrowError(battleId)).thenReturn(battle);

        doThrow(RuntimeException.class).when(repository).save(turn);

        assertThrows(UnknownViolationException.class, () -> service.createByBattle(battle));
    }
}
