package com.avanade.rpg.services;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.exceptions.*;
import com.avanade.rpg.mappers.CharacterMapper;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.repositories.CharacterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository repository;

    @Mock
    private CharacterMapper mapper;

    @InjectMocks
    private CharacterService service;

    private CharacterRequest heroRequest;
    private CharacterRequest monsterRequest;
    private Character hero;
    private Character monster;
    private CharacterResponse heroResponse;
    private CharacterResponse monsterResponse;

    @BeforeEach
    void setup() {
        heroRequest = new CharacterRequest(
                "Hero",
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 4,
                CharacterType.HERO.toString()
        );

        monsterRequest = new CharacterRequest(
                "Monster",
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 4,
                CharacterType.MONSTER.toString()
        );

        heroResponse = new CharacterResponse(UUID.randomUUID(), "Hero",
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 4,
                CharacterType.HERO);

        monsterResponse = new CharacterResponse(UUID.randomUUID(), "Monster",
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 10,
                (short) 4,
                CharacterType.MONSTER);


        hero = new Character();
        monster = new Character();
    }

    @Test
    @DisplayName("Should return all characters")
    void shouldReturnAllCharacters() {
        when(repository.findAll()).thenReturn(Arrays.asList(hero, monster));
        when(mapper.toResponse(hero)).thenReturn(heroResponse);
        when(mapper.toResponse(monster)).thenReturn(monsterResponse);

        List<CharacterResponse> responses = service.getAll();

        Assertions.assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("Should return character by ID")
    void shouldReturnCharacterById() {
        UUID id = UUID.randomUUID();
        Character hero = new Character();

        when(repository.findById(id)).thenReturn(Optional.of(hero));
        when(mapper.toResponse(hero)).thenReturn(heroResponse);

        CharacterResponse response = service.getById(id);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(heroResponse, response);
    }

    @Test
    @DisplayName("Should throw exception if character not found by ID")
    void shouldThrowExceptionIfCharacterNotFoundById() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(id));
    }

    @Test
    @DisplayName("Should be able to create a new Hero")
    void shouldCreateHero() {
        when(repository.existsByNameIgnoreCase(heroRequest.name())).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);
        when(mapper.toResponse(hero)).thenReturn(heroResponse);

        CharacterResponse response = service.create(heroRequest);

        Assertions.assertNotNull(response);

        verify(repository).existsByNameIgnoreCase(heroRequest.name());
        verify(mapper).toEntity(heroRequest);
        verify(repository).save(hero);
    }

    @Test
    @DisplayName("Should not create a new Hero when name already exists")
    void shouldNotCreateHeroWhenNameExists() {
        when(repository.existsByNameIgnoreCase(heroRequest.name())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(heroRequest));

        verify(repository).existsByNameIgnoreCase(heroRequest.name());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should be able to create a new Monster")
    void shouldCreateMonster() {
        when(repository.existsByNameIgnoreCase(monsterRequest.name())).thenReturn(false);
        when(mapper.toEntity(monsterRequest)).thenReturn(monster);
        when(mapper.toResponse(monster)).thenReturn(monsterResponse);

        CharacterResponse monsterResponse = service.create(monsterRequest);

        Assertions.assertNotNull(monsterResponse);
        verify(repository).existsByNameIgnoreCase(monsterRequest.name());
        verify(mapper).toEntity(monsterRequest);
        verify(repository).save(monster);
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when DataIntegrityViolationException is thrown")
    void shouldThrowConstraintViolationException() {
        Character hero = new Character();

        when(repository.existsByNameIgnoreCase("Hero")).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);
        doThrow(DataIntegrityViolationException.class).when(repository).save(any(Character.class));

        assertThrows(ConstraintViolationException.class, () -> service.create(heroRequest));
    }

    @Test
    @DisplayName("Should throw UnknownViolationException for unknown exceptions")
    void shouldThrowUnknownViolationException() {
        Character hero = new Character();

        when(repository.existsByNameIgnoreCase("Hero")).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);
        doThrow(RuntimeException.class).when(repository).save(any(Character.class));

        assertThrows(UnknownViolationException.class, () -> service.create(heroRequest));
    }

    @Test
    @DisplayName("Should update character by ID")
    void shouldUpdateCharacterById() {
        UUID id = UUID.randomUUID();
        hero.setName("testName");

        when(repository.findById(id)).thenReturn(Optional.of(hero));
        when(mapper.toEntity(id, heroRequest)).thenReturn(hero);
        when(mapper.toResponse(hero)).thenReturn(heroResponse);

        CharacterResponse response = service.update(id, heroRequest);

        Assertions.assertNotNull(response);
        verify(mapper).toEntity(id, heroRequest);
        verify(repository).save(hero);
        verify(mapper).toResponse(hero);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if character to update does not exist")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentCharacter() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(id, heroRequest));
    }

    @Test
    @DisplayName("Should throw ResourceAlreadyExistsException if updated character name already exists")
    void shouldThrowResourceAlreadyExistsExceptionWhenUpdatingToExistingName() {
        UUID id = UUID.randomUUID();
        Character oldCharacter = new Character();
        oldCharacter.setName("OldHero");

        when(repository.findById(id)).thenReturn(Optional.of(oldCharacter));
        when(repository.existsByNameIgnoreCase(heroRequest.name())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.update(id, heroRequest));
    }

    @Test
    @DisplayName("Should delete character by ID")
    void shouldDeleteCharacterById() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should not delete character by ID if it's in a Battle")
    void shouldNotDeleteCharacterByIdIfItsInABattle() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        when(repository.isCharacterInBattle(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.delete(id));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if character does not exist")
    void shouldThrowResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
    }
}
