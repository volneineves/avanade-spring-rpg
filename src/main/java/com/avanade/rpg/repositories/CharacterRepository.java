package com.avanade.rpg.repositories;

import com.avanade.rpg.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
