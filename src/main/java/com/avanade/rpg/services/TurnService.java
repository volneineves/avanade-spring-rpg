package com.avanade.rpg.services;

import com.avanade.rpg.amqp.HistoryPublisher;
import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.ActionType;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceNotFoundException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.factories.Action;
import com.avanade.rpg.factories.ActionFactory;
import com.avanade.rpg.mappers.TurnMapper;
import com.avanade.rpg.payloads.requests.ActionTurnRequest;
import com.avanade.rpg.payloads.requests.CreateTurnRequest;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.repositories.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.*;
import static com.avanade.rpg.enums.TurnStatus.*;
import static com.avanade.rpg.util.DiceUtil.rollDice;

@Service
public class TurnService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TurnService.class);
    private final TurnRepository repository;
    private final TurnMapper mapper;
    private final BattleService battleService;
    private final ActionFactory actionFactory;
    private final HistoryPublisher publisher;

    public TurnService(TurnRepository repository, TurnMapper mapper, BattleService battleService, ActionFactory actionFactory, HistoryPublisher publisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.battleService = battleService;
        this.actionFactory = actionFactory;
        this.publisher = publisher;
    }

    public TurnResponse create(CreateTurnRequest request) {
        Battle battle = battleService.findBattleByIdOrThrowError(request.battleId());
        battle.validateCanAddNewTurn();
        Turn turn = mapper.toEntity(battle, STARTED);
        saveOrThrowException(turn);
        return mapper.toResponse(turn);
    }

    public TurnResponse updateByAction(ActionTurnRequest request) {
        Turn turn = findTurnByIdOrThrowError(request.turnId());
        validateIfTurnFinished(turn);
        Character actingCharacter = findActingCharacter(turn, request.characterId());
        performCharacterAction(turn, actingCharacter, request.action());
        updateTurnStatus(turn, RUNNING);
        saveOrThrowException(turn);
        return mapper.toResponse(turn);
    }

    public TurnResponse calculateDamage(UUID turnId) {
        Turn turn = findTurnByIdOrThrowError(turnId);
        validateIfTurnFinished(turn);

        if (isDefendedSuccessfully(turn)) {
            updateTurnWithNoDamage(turn);
        } else {
            Character attacker = turn.getAttacker();
            Character defender = turn.getDefender();

            int damageValue = calculateDamageValue(attacker);
            applyDamageToDefender(defender, damageValue);
            updateTurnWithDamage(turn, damageValue, defender);
        }
        updateTurnStatus(turn, FINISHED);
        saveOrThrowException(turn);
        return mapper.toResponse(turn);
    }

    private boolean isDefendedSuccessfully(Turn turn) {
        return turn.getAttack() <= turn.getDefense();
    }

    private void updateTurnWithNoDamage(Turn turn) {
        turn.setDamage(0);
    }

    private int calculateDamageValue(Character attacker) {
        int numDice = attacker.getNumDice();
        DiceFaces faces = attacker.getFaces();
        return rollDice(numDice, faces) + attacker.getStrength();
    }

    private void applyDamageToDefender(Character defender, int damageValue) {
        short newHealth = (short) (defender.getHealth() - damageValue);
        defender.setHealth(newHealth <= 0 ? 0 : newHealth);
    }

    private void updateTurnWithDamage(Turn turn, int damageValue, Character defender) {
        turn.setDamage(damageValue);

        if (isDefenderDefeated(defender)) {
            turn.getBattle().setWinner(turn.getAttacker().getName());
            publisher.processHistoryBattle(turn.getBattle());
        }
    }

    private boolean isDefenderDefeated(Character defender) {
        return defender.getHealth() <= 0;
    }


    private void validateIfTurnFinished(Turn turn) {
        if (FINISHED.equals(turn.getStatus())) {
            throw new BadRequestException(TURN_ALREADY_FINISHED);
        }
    }

    private Character findActingCharacter(Turn turn, UUID characterId) {
        Battle battle = turn.getBattle();
        if (characterId.equals(battle.getInitiative().getId())) {
            return battle.getInitiative();
        }

        validateCharacterInitiative(battle, turn);

        if (characterId.equals(battle.getOpponent().getId())) {
            return battle.getOpponent();
        }

        throw new BadRequestException(INVALID_CHARACTER_ID);
    }

    private void validateCharacterInitiative(Battle battle, Turn turn) {
        if (turn.getAttacker() != battle.getInitiative() && turn.getDefender() != battle.getInitiative()) {
            throw new BadRequestException("Character initiative must to start the current turn before his opponent");
        }
    }

    private void performCharacterAction(Turn turn, Character character, ActionType actionType) {
        Action action = actionFactory.createAction(actionType);
        action.execute(turn, character);
    }

    private void updateTurnStatus(Turn turn, TurnStatus status) {
        turn.setStatus(status);
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
