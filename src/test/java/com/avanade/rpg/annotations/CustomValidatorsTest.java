package com.avanade.rpg.annotations;

import com.avanade.rpg.enums.CharacterType;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CustomValidatorsTest {

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("Should validate character type successfully")
    void shouldValidateCharacterTypeSuccessfully() {
        CharacterTypeValidator validator = new CharacterTypeValidator();

        for (CharacterType type : CharacterType.values()) {
            assertTrue(validator.isValid(type.name(), context));
        }

        assertFalse(validator.isValid("INVALID_TYPE", context));
    }

    @Test
    @DisplayName("Should validate dice faces successfully")
    void shouldValidateDiceFacesSuccessfully() {
        DiceFacesValidator validator = new DiceFacesValidator();

        short[] validFaces = {4, 6, 8, 10, 12, 20};
        for (short face : validFaces) {
            assertTrue(validator.isValid(face, context));
        }

        assertFalse(validator.isValid((short) 5, context));
        assertFalse(validator.isValid((short) 0, context));
        assertFalse(validator.isValid((short) -4, context));
    }
}
