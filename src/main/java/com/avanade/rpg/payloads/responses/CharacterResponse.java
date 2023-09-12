package com.avanade.rpg.payloads.responses;

import com.avanade.rpg.enums.CharacterType;

import java.util.UUID;

public record CharacterResponse(UUID id,
                                String name,
                                Short health,
                                Short strength,
                                Short defense,
                                Short agility,
                                Short numDice,
                                Short faces,
                                CharacterType type
) {
}
