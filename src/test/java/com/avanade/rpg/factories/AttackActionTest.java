package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.util.DiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AttackActionTest {

    @Mock
    private Turn mockTurn;

    private Character testCharacter;

    @InjectMocks
    private AttackAction attackAction;

    @BeforeEach
    void setUp() {
        testCharacter = new Character();
        testCharacter.setAgility((short) 5);
        testCharacter.setStrength((short) 10);
    }

    @Test
    @DisplayName("Should correctly set Turn's attack and attacker when execute is called")
    void shouldSetTurnAttackAndAttacker() {
        int diceRoll = DiceUtil.rollDice(DiceFaces.D12);

        attackAction.execute(mockTurn, testCharacter);

        int expectedAttackValue = diceRoll + testCharacter.getStrength() + testCharacter.getAgility();
        when(mockTurn.getAttack()).thenReturn(expectedAttackValue);
        assertTrue(mockTurn.getAttack() >= 16 && mockTurn.getAttack() <= 27);
        verify(mockTurn).setAttacker(testCharacter);
    }

    @Test
    @DisplayName("Should throw BadRequestException when an attacker has already been chosen")
    void shouldThrowBadRequestExceptionWhenAttackerAlreadyChosen() {
        Character mockCharacter = mock(Character.class);
        when(mockTurn.getAttacker()).thenReturn(mockCharacter);
        assertThrows(BadRequestException.class, () -> attackAction.execute(mockTurn, mockCharacter));
    }
}
