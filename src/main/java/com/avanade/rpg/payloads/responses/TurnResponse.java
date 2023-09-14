package com.avanade.rpg.payloads.responses;

import com.avanade.rpg.enums.TurnStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TurnResponse(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("numero_do_turno")
        Integer numTurn,

        @JsonProperty("valor_do_ataque")
        Integer attack,

        @JsonProperty("valor_da_defesa")
        Integer defense,

        @JsonProperty("dano")
        Integer damage,

        @JsonProperty("status")
        TurnStatus status
) {
}
