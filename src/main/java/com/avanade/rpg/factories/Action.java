package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;

public interface Action {
    void execute(Turn turn, Character character);
}
