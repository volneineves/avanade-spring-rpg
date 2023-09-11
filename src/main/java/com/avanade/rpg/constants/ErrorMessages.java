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
}
