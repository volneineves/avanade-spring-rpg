package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.ActionTurnRequest;
import com.avanade.rpg.payloads.requests.CreateTurnRequest;
import com.avanade.rpg.payloads.requests.UpdateTurnRequest;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.services.TurnService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.avanade.rpg.enums.ActionType.ATTACK;
import static com.avanade.rpg.enums.ActionType.DEFEND;

@RestController
@RequestMapping("/turns")
public class TurnController {

    private final TurnService turnService;

    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }

    @PostMapping
    public ResponseEntity<TurnResponse> create(@RequestBody CreateTurnRequest request) {
        TurnResponse turn = turnService.create(request);
        return ResponseEntity.status(201).body(turn);
    }

    @PatchMapping("/{id}/attack")
    public ResponseEntity<TurnResponse> attack(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), ATTACK);
        TurnResponse turn = turnService.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @PatchMapping("/{id}/defend")
    public ResponseEntity<TurnResponse> defend(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), DEFEND);
        TurnResponse turn = turnService.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @CacheEvict(value = "characters", allEntries = true)
    @PatchMapping("/{id}/calculate-damage")
    public ResponseEntity<TurnResponse> calculateDamage(@PathVariable UUID id) {
        TurnResponse updatedTurn = turnService.calculateDamage(id);
        return ResponseEntity.status(200).body(updatedTurn);
    }
}
