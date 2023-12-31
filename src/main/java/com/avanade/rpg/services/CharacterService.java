package com.avanade.rpg.services;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.exceptions.*;
import com.avanade.rpg.mappers.CharacterMapper;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.repositories.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.*;

@Service
public class CharacterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterService.class);
    private final CharacterRepository repository;
    private final CharacterMapper mapper;

    public CharacterService(CharacterRepository repository, CharacterMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CharacterResponse> getAll() {
        List<Character> characters = repository.findAll();
        return characters.stream().map(mapper::toResponse).toList();
    }

    public CharacterResponse getById(UUID id) {
        Character character = findCharacterByIdOrThrowError(id);
        return mapper.toResponse(character);
    }

    public CharacterResponse create(CharacterRequest request) {
        ensureCharacterDoesNotExistByName(request.name());
        Character character = mapper.toEntity(request);
        saveOrThrowException(character);
        return mapper.toResponse(character);
    }

    public CharacterResponse update(UUID id, CharacterRequest newCharacterRequest) {
        Character oldCharacter = findCharacterByIdOrThrowError(id);
        ensureNewCharacterNameIsValid(newCharacterRequest, oldCharacter);
        Character character = mapper.toEntity(id, newCharacterRequest);
        saveOrThrowException(character);
        return mapper.toResponse(character);
    }

    public void delete(UUID id) {
        ensureCharacterExistsById(id);
        ensureCharacterIsntABattle(id);
        deleteByIdOrThrowException(id);
    }

    public Character findCharacterByIdOrThrowError(UUID id) {
        LOGGER.info("Find character by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CHARACTER_NOT_FOUND + id));
    }

    private void ensureNewCharacterNameIsValid(CharacterRequest newCharacter, Character oldCharacter) {
        boolean namesArentEquals = !Objects.equals(oldCharacter.getName(), newCharacter.name());
        if (namesArentEquals) {
            ensureCharacterDoesNotExistByName(newCharacter.name());
        }
    }

    private void ensureCharacterDoesNotExistByName(String name) {
        LOGGER.info("Validating existence of character with name: {}", name);
        boolean existsByName = repository.existsByNameIgnoreCase(name);
        if (existsByName) {
            throw new ResourceAlreadyExistsException(CHARACTER_ALREADY_EXISTS + name);
        }
    }

    private void ensureCharacterExistsById(UUID id) {
        LOGGER.info("Validating existence of character with ID: {}", id);
        boolean characterExists = repository.existsById(id);
        if (!characterExists) {
            throw new ResourceNotFoundException(CHARACTER_NOT_FOUND + id);
        }
    }

    private void ensureCharacterIsntABattle(UUID id) {
        LOGGER.info("Validating existence of character in a Battle with ID: {}", id);
        boolean isCharacterInBattle = repository.isCharacterInBattle(id);
        if (isCharacterInBattle) {
            throw new BadRequestException(CHARACTER_IS_IN_A_BATTLE + id);
        }
    }


    private void saveOrThrowException(Character character) {
        handleExceptions(() -> repository.save(character));
        LOGGER.info("Successfully saved character with ID: {}", character.getId());
    }

    private void deleteByIdOrThrowException(UUID id) {
        handleExceptions(() -> repository.deleteById(id));
        LOGGER.info("Successfully deleted character with ID: {}", id);
    }

    private void handleExceptions(Runnable action) {
        try {
            action.run();
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(e.getMessage());
        } catch (Exception e) {
            throw new UnknownViolationException(e.getMessage());
        }
    }

}
