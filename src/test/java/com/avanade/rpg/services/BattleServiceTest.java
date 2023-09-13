package com.avanade.rpg.services;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.mappers.BattleMapper;
import com.avanade.rpg.payloads.requests.BattleRequest;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.repositories.BattleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private BattleRepository repository;

    @Mock
    private BattleMapper mapper;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private BattleService service;

    private UUID heroId;
    private UUID monsterId;
    private BattleRequest request;
    private Character hero;
    private Character monster;
    private Battle battle;
    private BattleResponse response;

    @BeforeEach
    void setUp() {
        heroId = UUID.randomUUID();
        monsterId = UUID.randomUUID();
        request = new BattleRequest(heroId, monsterId);

        hero = new Character();
        monster = new Character();
        battle = new Battle();
        response = mock(BattleResponse.class);
    }

    @Test
    @DisplayName("Should create a new battle")
    void shouldCreateNewBattle() {
        hero.setType(CharacterType.HERO);
        monster.setType(CharacterType.MONSTER);

        when(characterService.findCharacterByIdOrThrowError(heroId)).thenReturn(hero);
        when(characterService.findCharacterByIdOrThrowError(monsterId)).thenReturn(monster);
        when(mapper.toEntity(any(Character.class), any(Character.class))).thenReturn(battle);
        when(mapper.toResponse(battle)).thenReturn(response);

        BattleResponse actualResponse = service.create(request);

        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("Should throw BadRequestException if character type is not of type Character")
    void shouldThrowBadRequestExceptionForInvalidCharacter() {
        hero.setType(CharacterType.HERO);
        monster.setType(CharacterType.HERO);

        when(characterService.findCharacterByIdOrThrowError(heroId)).thenReturn(hero);
        when(characterService.findCharacterByIdOrThrowError(monsterId)).thenReturn(monster);

        assertThrows(BadRequestException.class, () -> service.create(request));
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when DataIntegrityViolationException is caught")
    void shouldThrowConstraintViolationException() {
        hero.setType(CharacterType.HERO);
        monster.setType(CharacterType.MONSTER);

        when(characterService.findCharacterByIdOrThrowError(heroId)).thenReturn(hero);
        when(characterService.findCharacterByIdOrThrowError(monsterId)).thenReturn(monster);
        when(mapper.toEntity(any(Character.class), any(Character.class))).thenReturn(battle);
        doThrow(DataIntegrityViolationException.class).when(repository).save(battle);

        assertThrows(ConstraintViolationException.class, () -> service.create(request));
    }

    @Test
    @DisplayName("Should throw UnknownViolationException for unhandled exceptions")
    void shouldThrowUnknownViolationException() {
        hero.setType(CharacterType.HERO);
        monster.setType(CharacterType.MONSTER);

        when(characterService.findCharacterByIdOrThrowError(heroId)).thenReturn(hero);
        when(characterService.findCharacterByIdOrThrowError(monsterId)).thenReturn(monster);
        when(mapper.toEntity(any(Character.class), any(Character.class))).thenReturn(battle);
        doThrow(RuntimeException.class).when(repository).save(battle);

        assertThrows(UnknownViolationException.class, () -> service.create(request));
    }
}
