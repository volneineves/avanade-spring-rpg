package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record DamageCalculationResponse(

        @JsonProperty("turno_anterior")
        @Schema(description = "Details of the previous turn in the battle", example = "{\"id\": \"a1b2c3d4-e5f6-7890-g1h2-3i4j567k89l0\", ...}")
        TurnResponse oldTurn,

        @JsonProperty("turno_novo")
        @Schema(description = "Details of the new turn in the battle", example = "{\"id\": \"m1n2o3p4-q5r6-7890-s1t2-3u4v567w89x0\", ...}")
        TurnResponse newTurn,

        @JsonProperty("ganhador_da_batalha")
        @Schema(description = "The winner of the battle", example = "Hero")
        String battleWinner
) {
}
