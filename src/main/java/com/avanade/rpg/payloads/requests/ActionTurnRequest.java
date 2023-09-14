package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.enums.ActionType;

import java.util.UUID;

public record ActionTurnRequest(UUID turnId, UUID characterId, ActionType action) {
}
