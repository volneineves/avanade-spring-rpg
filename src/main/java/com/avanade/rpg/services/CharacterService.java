package com.avanade.rpg.services;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.mappers.CharacterMapper;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.repositories.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterService.class);
    private final CharacterRepository repository;
    private final CharacterMapper mapper;

    public CharacterService(CharacterRepository repository, CharacterMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CharacterResponse save(CharacterRequest request) {
        ensureCharacterDoesNotExistByName(request.name());
        Character character = mapper.toEntity(request);
        saveOrThrowException(character);
        return mapper.toResponse(character);
    }

    private void ensureCharacterDoesNotExistByName(String name) {
        LOGGER.info("Validating existence of character with name: {}", name);
        boolean existsByName = repository.existsByName(name);
        if (existsByName) {
            LOGGER.warn("Character already exist with the name: {}", name); // TODO handle exception
        }
    }

    private void saveOrThrowException(Character character) {
        try {
            repository.save(character);
            LOGGER.info("Successfully saved character with ID: {}", character.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to save character: {} due to: {}", character, e.getMessage()); // TODO handle exception
        }
    }
}
