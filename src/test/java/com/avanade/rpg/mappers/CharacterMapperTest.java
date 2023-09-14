package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterMapperTest {

    private CharacterMapper mapper;
    private CharacterRequest request;

    @BeforeEach
    void setUp() {
        mapper = new CharacterMapper();
        request = new CharacterRequest("Guerreiro", (short) 100, (short) 10, (short) 5, (short) 2, (short) 1, (short) 6, "HERO");
    }

    @Test
    void shouldConvertToEntity() {
        Character entity = mapper.toEntity(request);

        assertEquals("Guerreiro", entity.getName());
        assertEquals(CharacterType.HERO, entity.getType());
        assertEquals(DiceFaces.D6, entity.getFaces());
    }

    @Test
    void shouldConvertToEntityWithId() {
        UUID id = UUID.randomUUID();

        Character entity = mapper.toEntity(id, request);

        assertEquals(id, entity.getId());
        assertEquals("Guerreiro", entity.getName());
        assertEquals(CharacterType.HERO, entity.getType());
        assertEquals(DiceFaces.D6, entity.getFaces());
    }

    @Test
    void shouldConvertToResponse() {
        UUID id = UUID.randomUUID();
        Character character = new Character();
        character.setId(id);
        character.setName("Guerreiro");
        character.setType(CharacterType.HERO);
        character.setFaces(DiceFaces.D6);

        CharacterResponse response = mapper.toResponse(character);

        assertEquals(id, response.id());
        assertEquals("Guerreiro", response.name());
        assertEquals(CharacterType.HERO, response.type());
        assertEquals((short) 6, response.faces());
    }
}
