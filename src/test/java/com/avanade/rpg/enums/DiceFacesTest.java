package com.avanade.rpg.enums;

import com.avanade.rpg.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiceFacesTest {

    @Test
    @DisplayName("Should return correct faces value for each enum constant")
    void shouldReturnCorrectFacesValueForEnumConstant() {
        assertEquals(4, DiceFaces.D4.getFaces());
        assertEquals(6, DiceFaces.D6.getFaces());
        assertEquals(8, DiceFaces.D8.getFaces());
        assertEquals(10, DiceFaces.D10.getFaces());
        assertEquals(12, DiceFaces.D12.getFaces());
        assertEquals(20, DiceFaces.D20.getFaces());
    }

    @Test
    @DisplayName("Should return correct enum constant for valid faces")
    void shouldReturnCorrectEnumConstantForValidFaces() {
        assertEquals(DiceFaces.D4, DiceFaces.valueOf(4));
        assertEquals(DiceFaces.D6, DiceFaces.valueOf(6));
        assertEquals(DiceFaces.D8, DiceFaces.valueOf(8));
        assertEquals(DiceFaces.D10, DiceFaces.valueOf(10));
        assertEquals(DiceFaces.D12, DiceFaces.valueOf(12));
        assertEquals(DiceFaces.D20, DiceFaces.valueOf(20));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid faces")
    void shouldThrowIllegalArgumentExceptionForInvalidFaces() {
        assertThrows(BadRequestException.class, () -> DiceFaces.valueOf(0));
        assertThrows(BadRequestException.class, () -> DiceFaces.valueOf(7));
        assertThrows(BadRequestException.class, () -> DiceFaces.valueOf(100));
    }
}
