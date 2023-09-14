package com.avanade.rpg.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateTurnRequest(
        @JsonProperty("id_do_personagem")
        @Schema(description = "The unique identifier of the character involved in the turn", example = "a8a767f8-435f-4d67-a77a-564ad4ee891a")
        @NotNull
        UUID characterId) {
}