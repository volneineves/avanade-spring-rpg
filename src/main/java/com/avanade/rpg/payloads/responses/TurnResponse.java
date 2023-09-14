package com.avanade.rpg.payloads.responses;

import com.avanade.rpg.enums.TurnStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record TurnResponse(
        @JsonProperty("id")
        @Schema(description = "The unique identifier of the turn", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @JsonProperty("numero_do_turno")
        @Schema(description = "The number of the turn in the sequence", example = "3")
        Integer numTurn,

        @JsonProperty("valor_do_ataque")
        @Schema(description = "The calculated attack value for this turn", example = "17")
        Integer attack,

        @JsonProperty("valor_da_defesa")
        @Schema(description = "The calculated defense value for this turn", example = "15")
        Integer defense,

        @JsonProperty("dano")
        @Schema(description = "The calculated damage value for this turn", example = "2")
        Integer damage,

        @JsonProperty("status")
        @Schema(description = "The status of the turn", example = "FINISHED")
        TurnStatus status
) {
}
