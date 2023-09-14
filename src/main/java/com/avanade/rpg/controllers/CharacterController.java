package com.avanade.rpg.controllers;

import com.avanade.rpg.payloads.requests.CharacterRequest;
import com.avanade.rpg.payloads.responses.CharacterResponse;
import com.avanade.rpg.services.CharacterService;
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

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Cacheable(value = "characters", key = "'all'")
    @GetMapping
    public ResponseEntity<List<CharacterResponse>> getAll() {
        List<CharacterResponse> characters = characterService.getAll();
        return ResponseEntity.ok(characters);
    }

    @Cacheable(value = "characters", key = "'all'")
    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getById(@PathVariable UUID id) {
        CharacterResponse character = characterService.getById(id);
        return ResponseEntity.ok(character);
    }

    @CacheEvict(value = "characters", allEntries = true)
    @PostMapping
    public ResponseEntity<CharacterResponse> create(@RequestBody @Valid CharacterRequest request) {
        CharacterResponse createdCharacter = characterService.create(request);
        return ResponseEntity.status(201).body(createdCharacter);
    }

    @CacheEvict(value = "characters", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<CharacterResponse> update(@PathVariable UUID id, @RequestBody CharacterRequest updatedCharacter) {
        CharacterResponse updated = characterService.update(id, updatedCharacter);
        return ResponseEntity.ok(updated);
    }

    @CacheEvict(value = "characters", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
