package com.avanade.rpg.enums;

public enum DiceFaces {
    D4((short) 4),
    D6((short) 6),
    D8((short) 8),
    D10((short) 10),
    D12((short) 12),
    D20((short) 20);

    private final short faces;

    DiceFaces(short faces) {
        this.faces = faces;
    }

    public short getFaces() {
        return this.faces;
    }

    public static DiceFaces valueOf(int faces) {
        for (DiceFaces dice : DiceFaces.values()) {
            if (dice.getFaces() == faces) {
                return dice;
            }
        }
        throw new IllegalArgumentException("No enum constant for faces: " + faces);
    }
}
