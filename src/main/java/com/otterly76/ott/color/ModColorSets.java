package com.otterly76.ott.color;

import java.util.List;

public final class ModColorSets {
    private ModColorSets() {}

    public static final List<ColorSet> ALL = List.of(

            new ColorSet("blush", 0xFFF6C5D4),
            new ColorSet("mimosa", 0xFFDD6DB3),
            new ColorSet("amethyst", 0xFFA840BB),
            new ColorSet("indigo", 0xFF623BB1),
            new ColorSet("cornflower", 0xFF3B7BC2),
            new ColorSet("ocean", 0xFF28A8BB),
            new ColorSet("forest", 0xFF3A8C59),
            new ColorSet("green_apple", 0xFF6FA21A),
            new ColorSet("key_lime", 0xFFBFD02E),
            new ColorSet("goldenrod", 0xFFFCAC2D),
            new ColorSet("paprika", 0xFFD45722),
            new ColorSet("sienna", 0xFF9A412C),
            new ColorSet("chocolate", 0xFF50392A),
            new ColorSet("charcoal", 0xFF32363A),
            new ColorSet("boulder", 0xFF727675),
            new ColorSet("mist", 0xFFCBCECB),

            new ColorSet("navy", 0xFF17215f)
    );

    public record ColorSet(String name, int color) {
        public ColorSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("ColorSet name must not be blank");
            }
        }
    }
}