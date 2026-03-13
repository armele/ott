package com.otterly76.ott.color;

import java.util.List;

public final class ModColorSets {
    private ModColorSets() {}

    public static final List<ColorSet> ALL = List.of(

            new ColorSet("blush", 0xF6C5D4),
            new ColorSet("mimosa", 0xDD6DB3),
            new ColorSet("amethyst", 0xA840BB),
            new ColorSet("indigo", 0x623BB1),
            new ColorSet("cornflower", 0x3B7BC2),
            new ColorSet("ocean", 0x28A8BB),
            new ColorSet("forest", 0x3A8C59),
            new ColorSet("green_apple", 0x6FA21A),
            new ColorSet("key_lime", 0xBFD02E),
            new ColorSet("goldenrod", 0xFCAC2D),
            new ColorSet("paprika", 0xD45722),
            new ColorSet("jasper", 0x9A412C),
            new ColorSet("chocolate", 0x50392A),
            new ColorSet("charcoal", 0x32363A),
            new ColorSet("boulder", 0x727675),
            new ColorSet("mist", 0xCBCECB)
    );

    public record ColorSet(String name, int color) {
        public ColorSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("ColorSet name must not be blank");
            }
        }
    }
}