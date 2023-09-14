package com.avanade.rpg.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateTurnRequest(@JsonProperty("id_do_personagem")
                                @NotNull UUID characterId) {
}