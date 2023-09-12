package com.avanade.rpg.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiceFacesValidator implements ConstraintValidator<ValidateDiceFaces, Short> {

    @Override
    public boolean isValid(Short value, ConstraintValidatorContext context) {
        return value == 4 || value == 6 || value == 8 || value == 10 || value == 12 || value == 20;
    }
}