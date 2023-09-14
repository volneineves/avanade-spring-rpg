package com.avanade.rpg.controllers;

import com.avanade.rpg.enums.TurnStatus;
import com.avanade.rpg.payloads.requests.ActionTurnRequest;
import com.avanade.rpg.payloads.requests.TurnFilterRequest;
import com.avanade.rpg.payloads.requests.UpdateTurnRequest;
import com.avanade.rpg.payloads.responses.DamageCalculationResponse;
import com.avanade.rpg.payloads.responses.StandardErrorResponse;
import com.avanade.rpg.payloads.responses.TurnResponse;
import com.avanade.rpg.services.TurnService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved turn by ID",
                    content = @Content(schema = @Schema(implementation = TurnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TurnResponse> getAllByParams(@PathVariable("id") UUID id) {
        TurnResponse turn = service.getById(id);
        return ResponseEntity.ok(turn);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of turns based on filter",
                    content = @Content(schema = @Schema(implementation = TurnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<TurnResponse>> getAllByParams(@RequestParam TurnStatus status, @RequestParam UUID battleId) {
        TurnFilterRequest turnFilterRequest = new TurnFilterRequest(status, battleId);
        List<TurnResponse> turns = service.getAllByFilter(turnFilterRequest);
        return ResponseEntity.ok(turns);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully executed an attack",
                    content = @Content(schema = @Schema(implementation = TurnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PatchMapping("/{id}/attack")
    public ResponseEntity<TurnResponse> attack(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), ATTACK);
        TurnResponse turn = service.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully executed a defense",
                    content = @Content(schema = @Schema(implementation = TurnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PatchMapping("/{id}/defend")
    public ResponseEntity<TurnResponse> defend(@PathVariable UUID id, @RequestBody UpdateTurnRequest request) {
        ActionTurnRequest turnActionRequest = new ActionTurnRequest(id, request.characterId(), DEFEND);
        TurnResponse turn = service.updateByAction(turnActionRequest);
        return ResponseEntity.status(200).body(turn);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated damage",
                    content = @Content(schema = @Schema(implementation = DamageCalculationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @CacheEvict(value = "characters", allEntries = true)
    @PatchMapping("/{id}/calculate-damage")
    public ResponseEntity<DamageCalculationResponse> calculateDamage(@PathVariable UUID id) {
        DamageCalculationResponse updatedTurn = service.calculateDamage(id);
        return ResponseEntity.status(200).body(updatedTurn);
    }
}
