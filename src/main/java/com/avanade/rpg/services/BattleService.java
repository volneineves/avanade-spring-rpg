package com.avanade.rpg.services;

import com.avanade.rpg.amqp.HistoryPublisher;
import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceNotFoundException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.mappers.BattleMapper;
import com.avanade.rpg.payloads.requests.BattleRequest;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.repositories.BattleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.*;
import static com.avanade.rpg.enums.CharacterType.HERO;
import static com.avanade.rpg.enums.CharacterType.MONSTER;
import static com.avanade.rpg.enums.DiceFaces.D20;
import static com.avanade.rpg.util.DiceUtil.rollDice;

@Service
public class BattleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BattleService.class);
    private final BattleRepository repository;
    private final BattleMapper mapper;
    private final CharacterService characterService;
    private final TurnService turnService;
    private final HistoryPublisher publisher;

    public BattleService(BattleRepository repository, BattleMapper mapper, CharacterService characterService, TurnService turnService, HistoryPublisher publisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.characterService = characterService;
        this.turnService = turnService;
        this.publisher = publisher;
    }

    public List<BattleResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BattleResponse getById(UUID id) {
        return mapper.toResponse(findBattleByIdOrThrowError(id));
    }

    @Transactional
    public BattleResponse create(BattleRequest request) {
        Character monster = validateAndGetCharacter(request.monsterId());
        Character hero = validateAndGetCharacter(request.heroId());
        Battle newBattle = initializeAndSaveBattle(hero, monster);

        createAndAddTurnToBattle(newBattle);

        return mapper.toResponse(newBattle);
    }

    private Character validateAndGetCharacter(UUID characterId) {
        Character character = characterService.findCharacterByIdOrThrowError(characterId);
        validateCharacterIsAlive(character);
        ensureCharacterIsAvailableForBattle(characterId);
        return character;
    }

    private void ensureCharacterIsAvailableForBattle(UUID characterId) {
        if (repository.existsOngoingBattleWithCharacter(characterId)) {
            throw new BadRequestException(CHARACTER_IS_IN_ANOTHER_BATTLE + characterId);
        }
    }

    private void validateCharacterIsAlive(Character character) {
        if (character.getHealth() <= 0) {
            throw new BadRequestException(CHARACTER_IS_DEAD + character.getId());
        }
    }

    private Battle initializeAndSaveBattle(Character hero, Character monster) {
        Battle newBattle = prepareBattle(hero, monster);
        saveOrThrowException(newBattle);
        return newBattle;
    }

    private void createAndAddTurnToBattle(Battle newBattle) {
        Turn turn = turnService.createByBattle(newBattle);
        newBattle.addTurn(turn);
    }

    private Battle prepareBattle(Character hero, Character monster) {
        validateCharacterType(hero, HERO);
        validateCharacterType(monster, MONSTER);
        return prepareNewBattleByInitiative(hero, monster);
    }

    private void validateCharacterType(Character character, CharacterType expectedType) {
        if (!character.getType().equals(expectedType)) {
            throw new BadRequestException(CHARACTER_IS_DIFFERENT + expectedType);
        }
    }

    private Battle prepareNewBattleByInitiative(Character hero, Character monster) {
        int heroInitiative = rollDice(D20);
        int monsterInitiative = rollDice(D20);

        Character initiator = heroInitiative > monsterInitiative ? hero : monster;
        Character opponent = (initiator.equals(hero)) ? monster : hero;

        return mapper.toEntity(initiator, opponent);
    }

    public Battle findBattleByIdOrThrowError(UUID id) {
        LOGGER.info("Find battle by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BATTLE_NOT_FOUND + id));
    }

    private void saveOrThrowException(Battle battle) {
        try {
            repository.save(battle);
            LOGGER.info("Successfully saved battle with ID: {}", battle.getId());
            publisher.processHistoryBattle(battle);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(e.getMessage());
        } catch (Exception e) {
            throw new UnknownViolationException(e.getMessage());
        }
    }
}
