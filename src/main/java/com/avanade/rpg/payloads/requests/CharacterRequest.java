package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.annotations.ValidateCharacterType;
import com.avanade.rpg.annotations.ValidateDiceFaces;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import static com.avanade.rpg.constants.ErrorMessages.*;

public record CharacterRequest(

        @JsonProperty("nome")
        @NotNull(message = NAME_CANNOT_BE_EMPTY_NULL)
        @Size(min = 1, max = 100, message = NAME_CANNOT_BE_EMPTY_NULL)
        String name,

        @JsonProperty("vida")
        @NotNull(message = HEALTH_MUST_BE_POSITIVE)
        @Positive(message = HEALTH_MUST_BE_POSITIVE)
        Short health,

        @JsonProperty("forca")
        @NotNull(message = STRENGTH_CANNOT_BE_NULL)
        Short strength,

        @JsonProperty("defesa")
        @NotNull(message = DEFENSE_CANNOT_BE_NULL)
        Short defense,

        @JsonProperty("agilidade")
        @NotNull(message = AGILITY_CANNOT_BE_NULL)
        Short agility,

        @JsonProperty("quantidade_de_dados")
        @NotNull(message = NUM_DICE_MUST_BE_POSITIVE)
        @Positive(message = NUM_DICE_MUST_BE_POSITIVE)
        Short numDice,

        @JsonProperty("faces_do_dado")
        @NotNull(message = FACES_MUST_BE_VALID)
        @ValidateDiceFaces(message = FACES_MUST_BE_VALID)
        Short faces,

        @JsonProperty("tipo")
        @ValidateCharacterType(message = TYPE_MUST_BE_VALID)
        @NotNull(message = TYPE_CANNOT_BE_NULL)
        String type
) {
}
