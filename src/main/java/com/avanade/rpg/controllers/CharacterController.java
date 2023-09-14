package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.payloads.responses.StandardErrorResponse;
import com.avanade.rpg.services.CharacterService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService service;

    public CharacterController(CharacterService characterService) {
        this.service = characterService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of characters"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @Cacheable(value = "characters", key = "'all'")
    @GetMapping
    public ResponseEntity<List<CharacterResponse>> getAll() {
        List<CharacterResponse> characters = service.getAll();
        return ResponseEntity.ok(characters);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved character by ID"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @Cacheable(value = "characters", key = "'all'")
    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(@PathVariable UUID id) {
        CharacterResponse character = service.getById(id);
        return ResponseEntity.ok(character);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new character"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @CacheEvict(value = "characters", allEntries = true)
    @PostMapping
    public ResponseEntity<CharacterResponse> create(@RequestBody @Valid CharacterRequest request) {
        CharacterResponse createdCharacter = service.create(request);
        return ResponseEntity.status(201).body(createdCharacter);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated character"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @CacheEvict(value = "characters", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<CharacterResponse> update(@PathVariable UUID id, @RequestBody CharacterRequest updatedCharacter) {
        CharacterResponse updated = service.update(id, updatedCharacter);
        return ResponseEntity.ok(updated);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted character"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @CacheEvict(value = "characters", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
