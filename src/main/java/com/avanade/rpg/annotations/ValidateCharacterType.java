package com.avanade.rpg.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CharacterTypeValidator.class})
public @interface ValidateCharacterType {
    String message() default "Invalid character type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}