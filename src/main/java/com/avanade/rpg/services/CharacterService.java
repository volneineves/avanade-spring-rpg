package com.avanade.rpg.services;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.exceptions.ConstraintViolationException;
import com.avanade.rpg.exceptions.ResourceAlreadyExistsException;
import com.avanade.rpg.exceptions.ResourceNotFoundException;
import com.avanade.rpg.exceptions.UnknownViolationException;
import com.avanade.rpg.mappers.CharacterMapper;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.repositories.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.CHARACTER_ALREADY_EXISTS;
import static com.avanade.rpg.constants.ErrorMessages.CHARACTER_NOT_FOUND;

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

    public CharacterResponse save(CharacterRequest request) {
        ensureCharacterDoesNotExistByName(request.name());
        Character character = mapper.toEntity(request);
        saveOrThrowException(character);
        return mapper.toResponse(character);
    }

    public void delete(UUID id) {
        ensureCharacterExistsById(id);
        deleteByIdOrThrowException(id);
    }

    private Character findCharacterByIdOrThrowError(UUID id) {
        LOGGER.info("Find character by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CHARACTER_NOT_FOUND + id));
    }

    private void ensureCharacterDoesNotExistByName(String name) {
        LOGGER.info("Validating existence of character with name: {}", name);
        boolean existsByName = repository.existsByName(name);
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
