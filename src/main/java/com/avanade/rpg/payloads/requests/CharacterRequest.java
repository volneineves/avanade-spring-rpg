package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.annotations.ValidateCharacterType;
import com.avanade.rpg.annotations.ValidateDiceFaces;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import static com.avanade.rpg.constants.ErrorMessages.*;

public record CharacterRequest(
                                @JsonProperty("nome")
                                @Schema(description = "Name of the character", example = "Guerrero")
                                @NotNull(message = NAME_CANNOT_BE_EMPTY_NULL)
                                @Size(min = 1, max = 100, message = NAME_CANNOT_BE_EMPTY_NULL)
                                String name,

                                @JsonProperty("vida")
                                @Schema(description = "Health points of the character", example = "95")
                                @NotNull(message = HEALTH_MUST_BE_POSITIVE)
                                @Positive(message = HEALTH_MUST_BE_POSITIVE)
                                Short health,

                                @JsonProperty("forca")
                                @Schema(description = "Strength of the character", example = "12")
                                @NotNull(message = STRENGTH_CANNOT_BE_NULL)
                                Short strength,

                                @JsonProperty("defesa")
                                @Schema(description = "Defense points of the character", example = "8")
                                @NotNull(message = DEFENSE_CANNOT_BE_NULL)
                                Short defense,

                                @JsonProperty("agilidade")
                                @Schema(description = "Agility points of the character", example = "7")
                                @NotNull(message = AGILITY_CANNOT_BE_NULL)
                                Short agility,

                                @JsonProperty("quantidade_de_dados")
                                @Schema(description = "Number of dice for the character", example = "2")
                                @NotNull(message = NUM_DICE_MUST_BE_POSITIVE)
                                @Positive(message = NUM_DICE_MUST_BE_POSITIVE)
                                Short numDice,

                                @JsonProperty("faces_do_dado")
                                @Schema(description = "Number of faces on the character's dice", example = "6")
                                @NotNull(message = FACES_MUST_BE_VALID)
                                @ValidateDiceFaces(message = FACES_MUST_BE_VALID)
                                Short faces,

                                @JsonProperty("tipo")
                                @Schema(description = "Type of the character", example = "HERO")
                                @ValidateCharacterType(message = TYPE_MUST_BE_VALID)
                                @NotNull(message = TYPE_CANNOT_BE_NULL)
                                String type)
{
}
