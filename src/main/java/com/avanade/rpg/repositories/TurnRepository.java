package com.avanade.rpg.repositories;

import com.avanade.rpg.entities.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TurnRepository extends JpaRepository<Turn, UUID> {
}
