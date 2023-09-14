package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DamageCalculationResponse(

        @JsonProperty("turno_anterior")
        TurnResponse oldTurn,

        @JsonProperty("turno_novo")
        TurnResponse newTurn,

        @JsonProperty("ganhador_da_batalha")
        String battleWinner
) {
}
