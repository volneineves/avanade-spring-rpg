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
import com.avanade.rpg.payloads.requests.TurnFilterRequest;
import com.avanade.rpg.payloads.responses.DamageCalculationResponse;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.repositories.TurnFilterRepository;
import com.avanade.rpg.repositories.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.*;
import static com.avanade.rpg.enums.TurnStatus.*;
import static com.avanade.rpg.util.DiceUtil.rollDice;

@Service
public class TurnService {

    private String winner = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(TurnService.class);
    private final TurnRepository repository;
    private final TurnFilterRepository filterRepository;
    private final TurnMapper mapper;
    private final ActionFactory actionFactory;
    private final HistoryPublisher publisher;

    public TurnService(TurnRepository repository, TurnFilterRepository filterRepository, TurnMapper mapper, ActionFactory actionFactory, HistoryPublisher publisher) {
        this.repository = repository;
        this.filterRepository = filterRepository;
        this.mapper = mapper;
        this.actionFactory = actionFactory;
        this.publisher = publisher;
    }

    public TurnResponse getById(UUID id) {
        Turn turn = findTurnByIdOrThrowError(id);
        return mapper.toResponse(turn);
    }

    public List<TurnResponse> getAllByFilter(TurnFilterRequest filterRequest) {
        List<Turn> turns = filterRepository.findByFilter(filterRequest);
        return turns.stream().map(mapper::toResponse).toList();
    }

    public Turn createByBattle(Battle battle) {
        validateTurnCreation(battle);
        Turn newTurn = initializeTurn(battle);
        saveOrThrowException(newTurn);
        return newTurn;
    }

    public TurnResponse updateByAction(ActionTurnRequest request) {
        Turn existingTurn = getUnfinishedTurnByIdOrThrow(request);
        Character actingCharacter = getActingCharacter(existingTurn, request.characterId());

        performActionAndUpdateStatus(existingTurn, actingCharacter, request.action());
        saveOrThrowException(existingTurn);
        return mapper.toResponse(existingTurn);
    }

    public DamageCalculationResponse calculateDamage(UUID turnId) {
        Turn existingTurn = findTurnByIdOrThrowError(turnId);
        processTurnDamage(existingTurn);
        saveOrThrowException(existingTurn);
        return prepareDamageCalculationResponse(existingTurn);
    }

    private DamageCalculationResponse prepareDamageCalculationResponse(Turn existingTurn) {
        TurnResponse newTurn = createAnotherTurnIfWinnerIsNull(existingTurn);
        TurnResponse oldTurn = mapper.toResponse(existingTurn);
        return new DamageCalculationResponse(oldTurn, newTurn, winner);
    }

    private TurnResponse createAnotherTurnIfWinnerIsNull(Turn existingTurn) {
        if (winner == null) {
            Battle battle = existingTurn.getBattle();
            Turn newTurn = createByBattle(battle);
            return mapper.toResponse(newTurn);
        }
        return null;
    }

    private void validateTurnCreation(Battle battle) {
        battle.validateCanAddNewTurn();
    }

    private Turn initializeTurn(Battle battle) {
        return mapper.toEntity(battle, STARTED);
    }

    private Character getActingCharacter(Turn turn, UUID characterId) {
        Battle battle = turn.getBattle();
        Character initiative = battle.getInitiative();
        Character opponent = battle.getOpponent();

        if (characterId.equals(initiative.getId())) {
            return initiative;
        }

        validateCharacterInitiative(battle, turn);

        if (characterId.equals(opponent.getId())) {
            return opponent;
        }

        throw new BadRequestException(INVALID_CHARACTER_ID);
    }

    private void validateCharacterInitiative(Battle battle, Turn turn) {
        if (!isCharacterInitiativeInTurn(battle, turn)) {
            throw new BadRequestException(BAD_OPPONENT_ORDER);
        }
    }

    private boolean isCharacterInitiativeInTurn(Battle battle, Turn turn) {
        return turn.getAttacker() == battle.getInitiative() || turn.getDefender() == battle.getInitiative();
    }


    private void performActionAndUpdateStatus(Turn turn, Character actingCharacter, ActionType actionType) {
        executeCharacterAction(turn, actingCharacter, actionType);
        updateTurnStatus(turn, RUNNING);
    }

    private void executeCharacterAction(Turn turn, Character character, ActionType actionType) {
        Action action = actionFactory.createAction(actionType);
        action.execute(turn, character);
    }

    private void processTurnDamage(Turn turn) {
        if (isDefendedSuccessfully(turn)) {
            applyNoDamage(turn);
        } else {
            applyCalculatedDamage(turn);
        }
        updateTurnStatus(turn, FINISHED);
    }

    private boolean isDefendedSuccessfully(Turn turn) {
        return turn.getAttack() <= turn.getDefense();
    }

    private void applyNoDamage(Turn turn) {
        turn.setDamage(0);
    }

    private void applyCalculatedDamage(Turn turn) {
        Character attacker = turn.getAttacker();
        Character defender = turn.getDefender();

        int damageValue = calculateDamageWithRollDice(attacker);
        applyDamage(defender, damageValue);

        updateTurnDamageAndWinner(turn, damageValue, defender);
    }

    private int calculateDamageWithRollDice(Character attacker) {
        return rollDice(attacker.getNumDice(), attacker.getFaces()) + attacker.getStrength();
    }

    private void applyDamage(Character defender, int damageValue) {
        short newHealth = (short) (defender.getHealth() - damageValue);
        defender.setHealth((short) Math.max(0, newHealth));
    }

    private void updateTurnDamageAndWinner(Turn turn, int damageValue, Character defender) {
        turn.setDamage(damageValue);

        if (isDefenderDefeated(defender)) {
            winner = turn.getAttacker().getName();
            processWinnerBattle(turn.getBattle());
        }
    }

    private void processWinnerBattle(Battle battle) {
        battle.setWinner(winner);
        publisher.processHistoryBattle(battle);
    }

    private boolean isDefenderDefeated(Character defender) {
        return defender.getHealth() <= 0;
    }

    private void updateTurnStatus(Turn turn, TurnStatus status) {
        turn.setStatus(status);
    }

    private Turn getUnfinishedTurnByIdOrThrow(ActionTurnRequest request) {
        Turn turn = findTurnByIdOrThrowError(request.turnId());
        validateTurnIsNotFinished(turn);
        return turn;
    }

    private void validateTurnIsNotFinished(Turn turn) {
        if (turn.getStatus().equals(FINISHED)) {
            throw new BadRequestException(TURN_ALREADY_FINISHED);
        }
    }

    private Turn findTurnByIdOrThrowError(UUID id) {
        LOGGER.info("Find turn by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TURN_NOT_FOUND + id));
    }

    private void saveOrThrowException(Turn turn) {
        try {
            repository.save(turn);
            LOGGER.info("Successfully saved turn with ID: {}", turn.getId());
            publisher.processHistoryTurn(turn);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(e.getMessage());
        } catch (Exception e) {
            throw new UnknownViolationException(e.getMessage());
        }
    }
}
