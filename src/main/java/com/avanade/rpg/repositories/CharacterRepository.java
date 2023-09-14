package com.avanade.rpg.repositories;

import com.avanade.rpg.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Battle b WHERE b.initiative.id = ?1 OR b.opponent.id = ?1")
    boolean isCharacterInBattle(UUID characterId);
}
