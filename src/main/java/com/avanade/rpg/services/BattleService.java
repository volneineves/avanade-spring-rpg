package com.avanade.rpg.services;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.mappers.BattleMapper;
import com.avanade.rpg.payloads.requests.BattleRequest;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.repositories.BattleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.avanade.rpg.enums.CharacterType.HERO;
import static com.avanade.rpg.enums.CharacterType.MONSTER;
import static com.avanade.rpg.enums.DiceFaces.D20;

@Service
public class BattleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BattleService.class);
    private static final Random RANDOM = new Random();

    private final BattleRepository repository;
    private final BattleMapper mapper;
    private final CharacterService characterService;

    public BattleService(BattleRepository repository, BattleMapper mapper, CharacterService characterService) {
        this.repository = repository;
        this.mapper = mapper;
        this.characterService = characterService;
    }

    public BattleResponse create(BattleRequest request) {
        Character monster = characterService.findCharacterByIdOrThrowError(request.monsterId());
        Character hero = characterService.findCharacterByIdOrThrowError(request.heroId());

        Battle battle = prepareNewBattleByHeroAndMonster(hero, monster);

        saveOrThrowException(battle);
        return mapper.toResponse(battle);
    }

    private Battle prepareNewBattleByHeroAndMonster(Character hero, Character monster) {
        validateCharacterType(hero, HERO);
        validateCharacterType(monster, MONSTER);
        Character initiativeCharacter = randomInitiativeCharacter(hero, monster);
        Character opponentCharacter = getOpponentCharacter(initiativeCharacter, hero, monster);

        return mapper.toEntity(initiativeCharacter, opponentCharacter);
    }

    private void validateCharacterType(Character character, CharacterType expectedType) {
        boolean isNotCorrectType = !character.getType().equals(expectedType);

        if (isNotCorrectType) {
            throw new BadRequestException("Character is different than expected type: " + expectedType);
        }
    }

    private Character getOpponentCharacter(Character initiativeCharacter, Character hero, Character monster) {
        return initiativeCharacter.equals(hero) ? monster : hero;
    }

    private Character randomInitiativeCharacter(Character hero, Character monster) {
        int heroInitiative = rollDice(D20);
        int monsterInitiative = rollDice(D20);
        return heroInitiative > monsterInitiative ? hero : monster;
    }

    private int rollDice(DiceFaces diceFaces) {
        return RANDOM.nextInt(diceFaces.getFaces()) + 1;
    }

    private void saveOrThrowException(Battle battle) {
        try {
            repository.save(battle);
            LOGGER.info("Successfully saved battle with ID: {}", battle.getId());
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(e.getMessage());
        } catch (Exception e) {
            throw new UnknownViolationException(e.getMessage());
        }
    }
}
