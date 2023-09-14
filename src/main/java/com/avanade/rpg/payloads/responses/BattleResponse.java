package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public record BattleResponse(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("iniciativa")
        CharacterResponse initiative,

        @JsonProperty("oponente")
        CharacterResponse opponent,

        @JsonProperty("turnos")
        List<TurnResponse> turns,

        @JsonProperty("vencedor")
        String winner) {
}
