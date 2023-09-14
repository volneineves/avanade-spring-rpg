package com.avanade.rpg.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTurnRequest(@JsonProperty("id_da_batalha")
                                @NotNull UUID battleId) {
}