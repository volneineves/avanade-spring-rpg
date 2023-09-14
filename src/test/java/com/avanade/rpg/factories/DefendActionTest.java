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
class DefendActionTest {

    @Mock
    private Turn mockTurn;

    private Character testCharacter;

    @InjectMocks
    private DefendAction defenderAction;

    @BeforeEach
    void setUp() {
        testCharacter = new Character();
        testCharacter.setAgility((short) 5);
        testCharacter.setDefense((short) 10);
    }

    @Test
    @DisplayName("Should correctly set Turn's defender and defender when execute is called")
    void shouldSetTurnDefenseAndDefender() {
        int diceRoll = DiceUtil.rollDice(DiceFaces.D12);

        defenderAction.execute(mockTurn, testCharacter);

        int expectedDefenseValue = diceRoll + testCharacter.getDefense() + testCharacter.getAgility();
        when(mockTurn.getDefense()).thenReturn(expectedDefenseValue);
        assertTrue(mockTurn.getDefense() >= 16 && mockTurn.getDefense() <= 27);
        verify(mockTurn).setDefender(testCharacter);
    }

    @Test
    @DisplayName("Should throw BadRequestException when an defender has already been chosen")
    void shouldThrowBadRequestExceptionWhenDefenderAlreadyChosen() {
        Character mockCharacter = mock(Character.class);
        when(mockTurn.getDefender()).thenReturn(mockCharacter);
        assertThrows(BadRequestException.class, () -> defenderAction.execute(mockTurn, mockCharacter));
    }
}