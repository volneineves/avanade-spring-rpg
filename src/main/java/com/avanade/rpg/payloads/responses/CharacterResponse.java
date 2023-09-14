package com.avanade.rpg.payloads.responses;

import com.avanade.rpg.enums.CharacterType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CharacterResponse(@JsonProperty("id")
                                @Schema(description = "Unique identifier for the character", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                                UUID id,

                                @JsonProperty("nome")
                                @Schema(description = "Name of the character", example = "Guerrero")
                                String name,

                                @JsonProperty("vida")
                                @Schema(description = "Health points of the character", example = "100")
                                Short health,

                                @JsonProperty("forca")
                                @Schema(description = "Strength of the character", example = "10")
                                Short strength,

                                @JsonProperty("defesa")
                                @Schema(description = "Defense points of the character", example = "8")
                                Short defense,

                                @JsonProperty("agilidade")
                                @Schema(description = "Agility points of the character", example = "7")
                                Short agility,

                                @JsonProperty("quantidade_de_dados")
                                @Schema(description = "Number of dice for the character", example = "2")
                                Short numDice,

                                @JsonProperty("faces_do_dado")
                                @Schema(description = "Number of faces on the character's dice", example = "6")
                                Short faces,

                                @JsonProperty("tipo")
                                @Schema(description = "Type of the character", example = "HERO")
                                CharacterType type
) {
}
