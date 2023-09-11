package com.avanade.rpg.annotations;

import com.avanade.rpg.enums.CharacterType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CharacterTypeValidator implements ConstraintValidator<ValidateCharacterType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (CharacterType type : CharacterType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}