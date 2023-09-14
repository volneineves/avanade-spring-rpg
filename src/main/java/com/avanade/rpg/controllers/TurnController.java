package com.avanade.rpg.controllers;

import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.payloads.requests.ActionTurnRequest;
import com.avanade.rpg.payloads.requests.TurnFilterRequest;
import com.avanade.rpg.payloads.requests.UpdateTurnRequest;
import com.avanade.rpg.payloads.responses.DamageCalculationResponse;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.services.TurnService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.avanade.rpg.enums.ActionType.ATTACK;
import static com.avanade.rpg.enums.ActionType.DEFEND;

@RestController
@RequestMapping("/turns")
public class TurnController {

    private final TurnService service;

    public TurnController(TurnService turnService) {
        this.service = turnService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnResponse> getAllByParams(@PathVariable("id") UUID id) {
        TurnResponse turn = service.getById(id);
        return ResponseEntity.ok(turn);
    }

    @GetMapping
    public ResponseEntity<List<TurnResponse>> getAllByParams(@RequestParam TurnStatus status, @RequestParam UUID battleId) {
        TurnFilterRequest turnFilterRequest = new TurnFilterRequest(status, battleId);
        List<TurnResponse> turns = service.getAllByFilter(turnFilterRequest);
        return ResponseEntity.ok(turns);
    }

    @PatchMapping("/{id}/attack")
    public ResponseEntity<TurnResponse> attack(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), ATTACK);
        TurnResponse turn = service.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @PatchMapping("/{id}/defend")
    public ResponseEntity<TurnResponse> defend(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), DEFEND);
        TurnResponse turn = service.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @CacheEvict(value = "characters", allEntries = true)
    @PatchMapping("/{id}/calculate-damage")
    public ResponseEntity<DamageCalculationResponse> calculateDamage(@PathVariable UUID id) {
        DamageCalculationResponse updatedTurn = service.calculateDamage(id);
        return ResponseEntity.status(200).body(updatedTurn);
    }
}
