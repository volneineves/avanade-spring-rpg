package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record TurnResponse(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("valor_do_ataque")
        short attackValue,

        @JsonProperty("valor_da_defesa")
        short defenseValue,

        @JsonProperty("dano")
        short damage) {
}
