package com.avanade.rpg.constants;

public class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String NAME_CANNOT_BE_EMPTY_NULL = "Field cannot be empty or null: name";
    public static final String HEALTH_MUST_BE_POSITIVE = "Field must be positive: health";
    public static final String STRENGTH_CANNOT_BE_NULL = "Field cannot be null: strength";
    public static final String DEFENSE_CANNOT_BE_NULL = "Field cannot be null: defense";
    public static final String AGILITY_CANNOT_BE_NULL = "Field cannot be null: agility";
    public static final String NUM_DICE_MUST_BE_POSITIVE = "Field must be positive: numDice";
    public static final String FACES_MUST_BE_VALID = "Field must be a valid dice face value (4, 6, 8, 10, 12 or 20): ";
    public static final String TYPE_CANNOT_BE_NULL = "Field cannot be null: type";
    public static final String TYPE_MUST_BE_VALID = "Field type must ve valid: MONSTER | HERO";
    public static final String CHARACTER_NOT_FOUND = "Character not found: ";
    public static final String CHARACTER_IS_IN_A_BATTLE = "Character could not be deleted because it's is on a battle: ";
    public static final String CHARACTER_ALREADY_EXISTS = "Character already exist: ";
    public static final String HERO_ID_CANNOT_BE_NULL = "Field cannot be null: heroId";
    public static final String MONSTER_ID_CANNOT_BE_NULL = "Field cannot be null: monsterId";
    public static final String BATTLE_NOT_FOUND = "Battle could not be found: ";
    public static final String TURN_NOT_FOUND = "Turn could not be found: ";
    public static final String CHARACTER_IS_DIFFERENT= "Character is different than expected type: ";
    public static final String TURN_ALREADY_FINISHED = "Turn already finished";
    public static final String INVALID_CHARACTER_ID = "Invalid character ID for the current battle";
    public static final String CHARACTER_IS_DEAD = "Character does not have health enough for starting a battle. Please, update it before try again: ";
    public static final String CHARACTER_IS_IN_ANOTHER_BATTLE = "Character is fighting in another battle. Please, choose another one, or wait for the finishing: ";


}
