package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.services.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public ResponseEntity<List<CharacterResponse>> getAll() {
        List<CharacterResponse> characters = characterService.getAll();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(@PathVariable UUID id) {
        CharacterResponse character = characterService.getById(id);
        return ResponseEntity.ok(character);
    }

    @PostMapping
    public ResponseEntity<CharacterResponse> create(@RequestBody @Valid CharacterRequest request) {
        CharacterResponse createdCharacter = characterService.save(request);
        return ResponseEntity.status(201).body(createdCharacter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
