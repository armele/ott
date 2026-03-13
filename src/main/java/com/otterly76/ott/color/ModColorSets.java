package com.otterly76.ott.color;

import java.util.List;

public final class ModColorSets {
    private ModColorSets() {}

    public static final List<ColorSet> ALL = List.of(

            new ColorSet("amber", 0xac6115),
            new ColorSet("aquamarine", 0x74c3d5),
            new ColorSet("honey", 0xfcac2d),
            new ColorSet("navy", 0x182665),
            new ColorSet("persimmon", 0xaa3514)
    );

    public record ColorSet(String name, int color) {
        public ColorSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("ColorSet name must not be blank");
            }
        }
    }
}