package com.avanade.rpg.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.HERO_ID_CANNOT_BE_NULL;
import static com.avanade.rpg.constants.ErrorMessages.MONSTER_ID_CANNOT_BE_NULL;

public record BattleRequest(
        @JsonProperty("id_do_heroi")
        @Schema(description = "Unique identifier of the hero", example = "a8a767f8-435f-4d67-a77a-564ad4ee891a")
        @NotNull(message = HERO_ID_CANNOT_BE_NULL)
        UUID heroId,

        @JsonProperty("id_do_monstro")
        @Schema(description = "Unique identifier of the monster", example = "f9302aa0-d14e-4a58-b132-1eb11dfea5ec")
        @NotNull(message = MONSTER_ID_CANNOT_BE_NULL)
        UUID monsterId) {
}