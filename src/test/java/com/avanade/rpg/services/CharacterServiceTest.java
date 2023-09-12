package com.avanade.rpg.services;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceAlreadyExistsException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.mappers.CharacterMapper;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.repositories.CharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.Assert.assertThrows;
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

        hero = new Character();
        monster = new Character();
    }

    @Test
    @DisplayName("Should be able to create a new Hero")
    void shouldCreateHero() {
        when(repository.existsByName(heroRequest.name())).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);

        service.save(heroRequest);

        verify(repository).existsByName(heroRequest.name());
        verify(mapper).toEntity(heroRequest);
        verify(repository).save(hero);
    }

    @Test
    @DisplayName("Should not create a new Hero when name already exists")
    void shouldNotCreateHeroWhenNameExists() {
        when(repository.existsByName(heroRequest.name())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(heroRequest));

        verify(repository).existsByName(heroRequest.name());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should be able to create a new Monster")
    void shouldCreateMonster() {
        when(repository.existsByName(monsterRequest.name())).thenReturn(false);
        when(mapper.toEntity(monsterRequest)).thenReturn(monster);

        service.save(monsterRequest);

        verify(repository).existsByName(monsterRequest.name());
        verify(mapper).toEntity(monsterRequest);
        verify(repository).save(monster);
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when DataIntegrityViolationException is thrown")
    void shouldThrowConstraintViolationException() {
        Character hero = new Character();

        when(repository.existsByName("Hero")).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);
        doThrow(DataIntegrityViolationException.class).when(repository).save(any(Character.class));

        assertThrows(ConstraintViolationException.class, () -> service.save(heroRequest));
    }

    @Test
    @DisplayName("Should throw UnknownViolationException for unknown exceptions")
    void shouldThrowUnknownViolationException() {
        Character hero = new Character();

        when(repository.existsByName("Hero")).thenReturn(false);
        when(mapper.toEntity(heroRequest)).thenReturn(hero);
        doThrow(RuntimeException.class).when(repository).save(any(Character.class));

        assertThrows(UnknownViolationException.class, () -> service.save(heroRequest));
    }
}
