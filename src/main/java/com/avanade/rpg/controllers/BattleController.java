package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.BattleRequest;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.services.BattleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/battles")
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping
    public ResponseEntity<List<BattleResponse>> getAll() {
        List<BattleResponse> battles = battleService.getAll();
        return ResponseEntity.ok(battles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BattleResponse> getById(@PathVariable("id") UUID id) {
        BattleResponse battle = battleService.getById(id);
        return ResponseEntity.ok(battle);
    }

    @PostMapping
    public ResponseEntity<BattleResponse> create(@RequestBody @Valid BattleRequest request) {
        BattleResponse battle = battleService.create(request);
        return ResponseEntity.status(201).body(battle);
    }
}
