package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.util.DiceUtil;

public class AttackAction implements Action {
    @Override
    public void execute(Turn turn, Character character) {
        int diceRoll = DiceUtil.rollDice(DiceFaces.D12);
        turn.setAttack(diceRoll + character.getStrength() + character.getAgility());
        turn.setAttacker(character);
    }
}
