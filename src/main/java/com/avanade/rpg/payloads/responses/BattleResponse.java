package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record BattleResponse(
        @JsonProperty("id")
        @Schema(description = "Unique identifier of the battle", example = "a1b2c3d4-e5f6-7890-g1h2-3i4j567k89l0")
        UUID id,

        @JsonProperty("iniciativa")
        @Schema(description = "Character who took the initiative in the battle")
        CharacterResponse initiative,

        @JsonProperty("oponente")
        @Schema(description = "Opponent character in the battle")
        CharacterResponse opponent,

        @JsonProperty("turnos")
        @Schema(description = "List of turns taken in the battle")
        List<TurnResponse> turns,

        @JsonProperty("vencedor")
        @Schema(description = "The winner of the battle", example = "HERO")
        String winner
) {
}
