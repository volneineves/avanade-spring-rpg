package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.BattleRequest;
import com.avanade.rpg.payloads.responses.BattleResponse;
import com.avanade.rpg.payloads.responses.StandardErrorResponse;
import com.avanade.rpg.services.BattleService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/battles")
public class BattleController {

    private final BattleService service;

    public BattleController(BattleService service) {
        this.service = service;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of battles"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<BattleResponse>> getAll() {
        List<BattleResponse> battles = service.getAll();
        return ResponseEntity.ok(battles);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved battle by ID"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BattleResponse> getById(@PathVariable("id") UUID id) {
        BattleResponse battle = service.getById(id);
        return ResponseEntity.ok(battle);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new battle"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BattleResponse> create(@RequestBody @Valid BattleRequest request) {
        BattleResponse battle = service.create(request);
        return ResponseEntity.status(201).body(battle);
    }

}
