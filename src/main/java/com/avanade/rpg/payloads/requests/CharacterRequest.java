package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.annotations.ValidateCharacterType;
import com.avanade.rpg.annotations.ValidateDiceFaces;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import static com.avanade.rpg.constants.ErrorMessages.*;

public record CharacterRequest(
        @NotNull(message = NAME_CANNOT_BE_EMPTY_NULL)
        @Size(min = 1, max = 100, message = NAME_CANNOT_BE_EMPTY_NULL)
        String name,

        @NotNull(message = HEALTH_MUST_BE_POSITIVE)
        @Positive(message = HEALTH_MUST_BE_POSITIVE)
        Short health,

        @NotNull(message = STRENGTH_CANNOT_BE_NULL)
        Short strength,

        @NotNull(message = DEFENSE_CANNOT_BE_NULL)
        Short defense,

        @NotNull(message = AGILITY_CANNOT_BE_NULL)
        Short agility,

        @NotNull(message = NUM_DICE_MUST_BE_POSITIVE)
        @Positive(message = NUM_DICE_MUST_BE_POSITIVE)
        Short numDice,

        @NotNull(message = FACES_MUST_BE_VALID)
        @ValidateDiceFaces(message = FACES_MUST_BE_VALID)
        Short faces,

        @ValidateCharacterType(message = TYPE_MUST_BE_VALID)
        @NotNull(message = TYPE_CANNOT_BE_NULL)
        String type
) {
}
