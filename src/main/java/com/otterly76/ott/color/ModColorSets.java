package com.otterly76.ott.color;

import java.util.List;

public final class ModColorSets {
    private ModColorSets() {}

    public static final List<ColorSet> ALL = List.of(

            new ColorSet("amber", 0xac6115),
            new ColorSet("aquamarine", 0x74c3d5),
            new ColorSet("bubblegum", 0xEF6DC0),
            new ColorSet("conifer", 0xB3D252),
            new ColorSet("honey", 0xfcac2d),
            new ColorSet("lavender", 0xB37ED6),
            new ColorSet("maroon", 0x8D0000),
            new ColorSet("mint_green", 0x82DDB0),
            new ColorSet("navy", 0x182665),
            new ColorSet("peach", 0xCDB087),
            new ColorSet("persimmon", 0xaa3514),
            new ColorSet("ruby", 0xFA2763),
            new ColorSet("sapphire", 0x5145F2),
            new ColorSet("spring_green", 0xC2FE98),
            new ColorSet("teal", 0x3B9189),
            new ColorSet("turquiose", 0x0091C6),
            new ColorSet("wine", 0x721E5B),
            new ColorSet("blush", 0xF6C5D4),
            new ColorSet("mimosa", 0xDD6DB3),
            new ColorSet("amethyst", 0xA840BB),
            new ColorSet("indigo", 0x623BB1),
            new ColorSet("cornflower", 0x3B7BC2),
            new ColorSet("ocean", 0x28A8BB),
            new ColorSet("forest", 0x3A8C59),
            new ColorSet("jade", 0x6FA21A),
            new ColorSet("key_lime", 0xBFD02E),
            new ColorSet("gold", 0xFCAC2D),
            new ColorSet("paprika", 0xD45722),
            new ColorSet("jasper", 0x9A412C),
            new ColorSet("chocolate", 0x50392A),
            new ColorSet("deepslate", 0x32363A),
            new ColorSet("boulder", 0x727675),
            new ColorSet("cloud", 0xCBCECB)
    );

    public record ColorSet(String name, int color) {
        public ColorSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("ColorSet name must not be blank");
            }
        }
    }
}