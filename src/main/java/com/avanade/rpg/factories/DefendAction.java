package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.util.DiceUtil;

import static com.avanade.rpg.constants.ErrorMessages.DEFENSER_ALREADY_CHOOSE;

public class DefendAction implements Action {
    @Override
    public void execute(Turn turn, Character character) {
        validateDefenseCanChange(turn);
        int diceRoll = DiceUtil.rollDice(DiceFaces.D12);
        turn.setDefense(diceRoll + character.getDefense() + character.getAgility());
        turn.setDefender(character);
    }

    private void validateDefenseCanChange(Turn turn) {
        if (turn.getDefender() != null) {
            throw new BadRequestException(DEFENSER_ALREADY_CHOOSE);
        }
    }
}