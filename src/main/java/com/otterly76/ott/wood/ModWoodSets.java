package com.otterly76.ott.wood;

import java.util.List;

public final class ModWoodSets {
    private ModWoodSets() {}

    public static final List<WoodSet> ALL = List.of(
            new WoodSet("starlight"),
            new WoodSet("midnight")
    );

    public record WoodSet(String name) {
        public WoodSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("WoodSet name must not be blank");
            }
        }
    }
}