package com.avanade.rpg.factories;

import com.avanade.rpg.entities.Character;
import com.avanade.rpg.entities.Turn;
import com.avanade.rpg.enums.DiceFaces;
import com.avanade.rpg.exceptions.BadRequestException;
import com.avanade.rpg.util.DiceUtil;

import static com.avanade.rpg.constants.ErrorMessages.ATTACKER_ALREADY_CHOOSE;

public class AttackAction implements Action {
    @Override
    public void execute(Turn turn, Character character) {
        validateAttackCanChange(turn);
        int diceRoll = DiceUtil.rollDice(DiceFaces.D12);
        turn.setAttack(diceRoll + character.getStrength() + character.getAgility());
        turn.setAttacker(character);
    }

    private void validateAttackCanChange(Turn turn) {
        if (turn.getAttacker() != null) {
            throw new BadRequestException(ATTACKER_ALREADY_CHOOSE);
        }
    }
}
