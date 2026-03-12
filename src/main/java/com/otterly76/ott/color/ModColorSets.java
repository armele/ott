package com.otterly76.ott.color;

import java.util.List;

public final class ModColorSets {
    private ModColorSets() {}

    public static final List<ColorSet> ALL = List.of(

            new ColorSet("amber", 0xab6015),
            new ColorSet("aquamarine", 0x72A0C1),
            new ColorSet("carnelian", 0xd04b15),
            new ColorSet("honey", 0xf7a420),
            new ColorSet("navy", 0x000080)
    );

    public record ColorSet(String name, int color) {
        public ColorSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("ColorSet name must not be blank");
            }
        }
    }
}