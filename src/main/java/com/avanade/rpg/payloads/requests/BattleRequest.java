package com.avanade.rpg.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.avanade.rpg.constants.ErrorMessages.HERO_ID_CANNOT_BE_NULL;
import static com.avanade.rpg.constants.ErrorMessages.MONSTER_ID_CANNOT_BE_NULL;

public record BattleRequest(
        @JsonProperty("id_do_heroi")
        @NotNull(message = HERO_ID_CANNOT_BE_NULL)
        UUID heroId,

        @JsonProperty("id_do_monstro")
        @NotNull(message = MONSTER_ID_CANNOT_BE_NULL)
        UUID monsterId) {
}