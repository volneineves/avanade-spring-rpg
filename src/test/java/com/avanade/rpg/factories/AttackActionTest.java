package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.util.DiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AttackActionTest {

    private Turn turn;
    private Character character;
    private AttackAction action;

    @BeforeEach
    void setUp() {
        turn = new Turn();
        character = new Character();
        character.setAgility((short) 5);
        character.setStrength((short) 10);
        action = new AttackAction();
    }

    @Test
    @DisplayName("Should correctly set Turn defense and defender when execute is called")
    void shouldSetTurnDefenseAndDefender() {
        action.execute(turn, character);
        DiceUtil.rollDice(DiceFaces.D12);

        assertTrue(turn.getAttack() >= 16 && turn.getAttack() <= 27);
        assertEquals(character, turn.getAttacker());
    }
}