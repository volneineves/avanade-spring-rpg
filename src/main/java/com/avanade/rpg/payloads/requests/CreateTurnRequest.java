package com.avanade.rpg.payloads.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTurnRequest(@NotNull UUID battleId) {
}