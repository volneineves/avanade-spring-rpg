package com.avanade.rpg.payloads.responses;

public record StandardErrorResponse(Integer status, String message, String timestamp) {
}
