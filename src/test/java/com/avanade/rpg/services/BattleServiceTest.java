package com.avanade.rpg.services;

import com.avanade.rpg.entities.Battle;
import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    private UUID battleId;
    private BattleRequest request;
    private Character hero;
    private Character monster;
    private Battle battle;
    private BattleResponse response;

    @BeforeEach
    void setUp() {
        heroId = UUID.randomUUID();
        monsterId = UUID.randomUUID();
        battleId = UUID.randomUUID();
        request = new BattleRequest(heroId, monsterId);

        hero = new Character();
        monster = new Character();
        battle = new Battle();
        response = mock(BattleResponse.class);
    }

    @Test
    @DisplayName("Should return a list of all battles")
    void shouldReturnAllBattles() {

        BattleResponse response2 = mock(BattleResponse.class);
        Battle battle2 = new Battle();
        List<Battle> battleList = List.of(battle, battle2);
        List<BattleResponse> expectedResponseList = List.of(response, response2);

        when(repository.findAll()).thenReturn(battleList);
        when(mapper.toResponse(battle)).thenReturn(response);
        when(mapper.toResponse(battle2)).thenReturn(response2);

        List<BattleResponse> actualResponseList = service.getAll();

        assertEquals(expectedResponseList, actualResponseList);
    }

    @Test
    @DisplayName("Should get Battle by ID")
    void shouldGetBattleById() {

        when(repository.findById(battleId)).thenReturn(Optional.of(battle));
        when(mapper.toResponse(battle)).thenReturn(response);

        BattleResponse battleResponse = service.getById(battleId);

        assertNotNull(battleResponse);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getting a Battle by ID that does not exist")
    void shouldThrowResourceNotFoundExceptionWhenBattleNotFoundById() {
        UUID battleId = UUID.randomUUID();

        when(repository.findById(battleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(battleId));
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
