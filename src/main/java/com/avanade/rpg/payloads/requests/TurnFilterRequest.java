package com.avanade.rpg.payloads.requests;

import com.avanade.rpg.enums.TurnStatus;

import java.util.UUID;

public record TurnFilterRequest(TurnStatus status, UUID battleId) {
}
