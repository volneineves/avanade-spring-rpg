package com.avanade.rpg.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Positive
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DiceFacesValidator.class})
public @interface ValidateDiceFaces {
    String message() default "Invalid number of faces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}