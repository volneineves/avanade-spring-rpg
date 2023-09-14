package com.avanade.rpg.utils;

import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.util.DiceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceUtilTest {

    @Test
    @DisplayName("Should return a number between 1 and the number of faces for a single dice roll")
    void shouldReturnNumberBetweenOneAndNumberOfFacesForSingleDiceRoll() {
        for (DiceFaces face : DiceFaces.values()) {
            int result = DiceUtil.rollDice(face);
            assertTrue(result >= 1 && result <= face.getFaces(),
                    "Dice roll should be between 1 and " + face.getFaces() + " but was " + result);
        }
    }

    @Test
    @DisplayName("Should return a sum between the number of dice and the maximum possible sum for multiple dice rolls")
    void shouldReturnSumBetweenNumberOfDiceAndMaximumPossibleSumForMultipleDiceRolls() {
        int numDice = 5;

        for (DiceFaces face : DiceFaces.values()) {
            int result = DiceUtil.rollDice(numDice, face);
            int lowerBound = numDice;
            int upperBound = numDice * face.getFaces();
            assertTrue(result >= lowerBound && result <= upperBound,
                    "Sum of dice rolls should be between " + lowerBound + " and " + upperBound + " but was " + result);
        }
    }
}
