package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.enums.CharacterType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CharacterRequest(@NotNull @Size(min = 1, max = 100) String name,
                               @NotNull @Positive Short health,
                               @NotNull Short strength,
                               @NotNull Short defense,
                               @NotNull Short agility,
                               @NotNull @Positive Short numDice,
                               @NotNull Short faces,
                               @NotNull CharacterType type
) {
}
