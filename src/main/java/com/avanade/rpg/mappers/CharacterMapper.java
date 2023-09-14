package com.avanade.rpg.mappers;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.enums.CharacterType;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CharacterMapper {

    public Character toEntity(CharacterRequest request) {
        Character character = new Character();
        character.setName(request.name());
        character.setHealth(request.health());
        character.setStrength(request.strength());
        character.setDefense(request.defense());
        character.setAgility(request.agility());
        character.setNumDice(request.numDice());
        character.setFaces(DiceFaces.valueOf(request.faces()));
        character.setType(CharacterType.valueOf(request.type()));
        return character;
    }

    public Character toEntity(UUID id, CharacterRequest request) {
        Character character = toEntity(request);
        character.setId(id);
        return character;
    }

    public CharacterResponse toResponse(Character character) {
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                character.getHealth(),
                character.getStrength(),
                character.getDefense(),
                character.getAgility(),
                character.getNumDice(),
                character.getFaces().getFaces(),
                character.getType()
        );
    }
}
