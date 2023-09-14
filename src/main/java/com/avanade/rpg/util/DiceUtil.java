package com.avanade.rpg.util;

import com.avanade.rpg.enums.DiceFaces;

import java.util.Random;

public class DiceUtil {

    private static final Random RANDOM = new Random();

    private DiceUtil() {
    }

    public static int rollDice(DiceFaces diceFaces) {
        return RANDOM.nextInt(diceFaces.getFaces()) + 1;
    }

    public static int rollDice(int numDice, DiceFaces diceFaces) {
        int sum = 0;
        for (int i = 0; i < numDice; i++) {
            sum += rollDice(diceFaces);
        }
        return sum;
    }
}
